package com.david.hlp.SpringBootWork.system.service.imp;

import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.enumentity.DefaultRole;
import com.david.hlp.SpringBootWork.system.mapper.RoleMapper;
import com.david.hlp.SpringBootWork.system.requestentity.AuthenticationRequest;
import com.david.hlp.SpringBootWork.system.requestentity.RegisterRequest;
import com.david.hlp.SpringBootWork.system.responsentity.AuthenticationResponse;
import com.david.hlp.SpringBootWork.system.auth.JwtService;
import com.david.hlp.SpringBootWork.system.token.Token;
import com.david.hlp.SpringBootWork.system.token.TokenRepository;
import com.david.hlp.SpringBootWork.system.token.TokenType;
import com.david.hlp.SpringBootWork.system.entity.User;
import com.david.hlp.SpringBootWork.system.Repository.UserRepository;
import com.david.hlp.SpringBootWork.system.util.RedisCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务类。
 * <p>
 * 提供用户注册、认证（登录）、令牌刷新等核心功能。
 * <p>
 * 使用 Spring Security、JWT 和数据库进行身份验证和令牌管理。
 */
@Service
@RequiredArgsConstructor // 自动生成包含所有依赖项的构造函数
public class AuthenticationServiceImp {

  // 用户数据访问仓库
  private final UserRepository repository;

  // 用于访问和管理 JWT 令牌的仓库
  private final TokenRepository tokenRepository;

  // 用于密码加密和验证的工具
  private final PasswordEncoder passwordEncoder;

  // JWT 服务类，用于生成和验证 JSON Web Tokens
  private final JwtService jwtService;

  // Spring Security 提供的用户认证管理器
  private final AuthenticationManager authenticationManager;

  // 数据库操作用户角色的映射接口
  private final RoleMapper roleMapper;

  // 默认角色名称，来自枚举 DefaultRole
  private final String defaultRoleName = DefaultRole.GUEST.getRole();

  // Redis 缓存工具类
  private final RedisCache redisCache;

  /**
   * 用户注册功能。
   * <p>
   * 验证邮箱和验证码的有效性，保存新用户到数据库，
   * <p>
   * 并生成和返回访问令牌及刷新令牌。
   *
   * @param request 包含用户注册信息的请求对象。
   * @return 返回包含访问令牌和刷新令牌的响应。
   */
  public AuthenticationResponse register(RegisterRequest request) {
    // 检查 Redis 中是否存在验证码缓存
    if (!redisCache.hasKey(request.getEmail())) {
      throw new RuntimeException("验证邮箱不存在");
    }

    // 验证用户输入的验证码是否正确
    if (!redisCache.getCacheObject(request.getEmail()).equals(request.getVerificationCode())) {
      throw new RuntimeException("验证码错误");
    }

    // 从数据库中获取默认角色对象
    Role defaultRole = roleMapper.selectByRoleName(defaultRoleName);

    // 创建并构建用户对象
    var user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword())) // 对用户密码加密
            .role(defaultRole)
            .build();

    // 将用户保存到数据库
    var savedUser = repository.save(user);

    // 生成 JWT 访问令牌和刷新令牌
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // 将生成的令牌保存到数据库
    saveUserToken(savedUser, jwtToken);

    // 删除 Redis 中的验证码缓存
    redisCache.deleteObject(request.getEmail());

    // 返回带有令牌的响应对象
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * 用户认证（登录）功能。
   * <p>
   * 验证用户凭据，生成访问令牌和刷新令牌，并保存到数据库和 Redis。
   *
   * @param request 包含用户登录信息的请求对象。
   * @return 返回包含访问令牌和刷新令牌的响应。
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // 验证用户凭据（邮箱和密码）
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    // 从数据库中获取用户信息
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();

    // 设置用户的角色信息
    user.setRole(roleMapper.selectByRoleName(user.getRole()));

    // 生成访问令牌和刷新令牌
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // 撤销用户的所有有效令牌
    revokeAllUserTokens(user);

    // 保存新的访问令牌到数据库
    saveUserToken(user, jwtToken);

    // 将令牌对存入 Redis，设置有效期为 12 小时
    redisCache.setCacheObject(jwtToken, refreshToken, 12, TimeUnit.HOURS);

    // 返回认证响应对象
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * 保存用户令牌到数据库。
   *
   * @param user 用户对象。
   * @param jwtToken 生成的访问令牌。
   */
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER) // 令牌类型为 BEARER
            .expired(false) // 设置为未过期
            .revoked(false) // 设置为未撤销
            .build();
    tokenRepository.save(token); // 保存令牌到数据库
  }

  /**
   * 撤销用户的所有有效令牌。
   * <p>
   * 设置所有有效令牌为过期并撤销，并从 Redis 中删除。
   *
   * @param user 用户对象。
   */
  private void revokeAllUserTokens(User user) {
    // 查询用户的所有有效令牌
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;

    // 删除 Redis 中的对应令牌
    validUserTokens.forEach(token -> {
      redisCache.deleteObject(token.token);
    });

    // 将所有令牌设置为过期和撤销状态
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    // 保存更新后的令牌状态到数据库
    tokenRepository.saveAll(validUserTokens);
  }

  /**
   * 刷新用户的访问令牌。
   * <p>
   * 验证刷新令牌的有效性并生成新的访问令牌。
   *
   * @param request HTTP 请求对象，包含旧的刷新令牌。
   * @param response HTTP 响应对象，用于返回新的访问令牌。
   * @throws IOException 如果发生 I/O 错误。
   */
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    // 从请求头中获取认证信息
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;

    // 如果请求头为空或不以 "Bearer " 开头，直接返回
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    // 提取刷新令牌
    refreshToken = authHeader.substring(7);

    // 从刷新令牌中提取用户名
    userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail != null) {
      // 从数据库查找用户
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();

      // 验证刷新令牌是否有效
      if (jwtService.isTokenValid(refreshToken, user)) {
        // 生成新的访问令牌
        var accessToken = jwtService.generateToken(user);

        // 撤销用户的所有旧令牌
        revokeAllUserTokens(user);

        // 保存新的访问令牌
        saveUserToken(user, accessToken);

        // 构建认证响应对象
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // 将响应对象写入 HTTP 输出流
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}