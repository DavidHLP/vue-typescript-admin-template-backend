package com.david.hlp.SpringBootWork.system.auth.service;

import com.david.hlp.SpringBootWork.system.auth.entity.Role;
import com.david.hlp.SpringBootWork.system.auth.enumentity.DefaultRole;
import com.david.hlp.SpringBootWork.system.auth.mapper.RoleMapper;
import com.david.hlp.SpringBootWork.system.auth.requestentity.AuthenticationRequest;
import com.david.hlp.SpringBootWork.system.auth.requestentity.RegisterRequest;
import com.david.hlp.SpringBootWork.system.auth.responsentity.AuthenticationResponse;
import com.david.hlp.SpringBootWork.system.auth.config.JwtService;
import com.david.hlp.SpringBootWork.system.auth.token.Token;
import com.david.hlp.SpringBootWork.system.auth.token.TokenRepository;
import com.david.hlp.SpringBootWork.system.auth.token.TokenType;
import com.david.hlp.SpringBootWork.system.auth.entity.User;
import com.david.hlp.SpringBootWork.system.auth.Repository.UserRepository;
import com.david.hlp.SpringBootWork.system.auth.util.RedisCache;
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
 *
 * 提供用户注册、认证（登录）、令牌刷新等核心功能。
 * 使用 Spring Security、JWT 和数据库进行身份验证和令牌管理。
 */
@Service
@RequiredArgsConstructor // 自动生成包含所有依赖项的构造函数
public class AuthenticationService {

  private final UserRepository repository; // 用于访问用户数据的仓库
  private final TokenRepository tokenRepository; // 用于访问令牌数据的仓库
  private final PasswordEncoder passwordEncoder; // 密码加密工具
  private final JwtService jwtService; // JWT 服务类，用于生成和验证令牌
  private final AuthenticationManager authenticationManager; // 用于认证用户身份
  private final RoleMapper roleMapper;
  private final String defaultRoleName = DefaultRole.ADMIN.getRole();
  private final RedisCache redisCache;


  /**
   * 用户注册功能。
   *
   * @param request 包含用户注册信息的请求对象。
   * @return 返回包含访问令牌和刷新令牌的响应。
   */
  public AuthenticationResponse register(RegisterRequest request) {
    // 构建新用户对象
    Role defaultRole = roleMapper.selectByRoleName(defaultRoleName);
    var user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword())) // 加密用户密码
            .role(defaultRole)
            .build();

    // 保存用户到数据库
    var savedUser = repository.save(user);

    // 生成访问令牌和刷新令牌
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // 保存用户令牌到数据库
    saveUserToken(savedUser, jwtToken);

    // 返回认证响应
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * 用户认证（登录）功能。
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

    // 从数据库获取用户信息
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();

    user.setRole(roleMapper.selectByRoleName(user.getRole()));

    // 生成访问令牌和刷新令牌
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    // 撤销用户的所有有效令牌
    revokeAllUserTokens(user);

    // 保存新生成的访问令牌
    saveUserToken(user, jwtToken);

    redisCache.setCacheObject(jwtToken, refreshToken,12, TimeUnit.HOURS);

    // 返回认证响应
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  /**
   * 保存用户令牌。
   *
   * @param user 用户对象。
   * @param jwtToken 生成的访问令牌。
   */
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  /**
   * 撤销用户的所有有效令牌。
   *
   * @param user 用户对象。
   */
  private void revokeAllUserTokens(User user) {
    // 查询用户的所有有效令牌
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;

    // 清除redis中Token
    validUserTokens.forEach(token -> {
      redisCache.deleteObject(token.token);
    });

    // 设置所有令牌为失效状态
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });

    // 保存更新后的令牌状态
    tokenRepository.saveAll(validUserTokens);
  }

  /**
   * 刷新用户的访问令牌。
   *
   * @param request HTTP请求对象，包含旧的刷新令牌。
   * @param response HTTP响应对象，用于返回新的访问令牌。
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

        // 撤销所有旧令牌
        revokeAllUserTokens(user);

        // 保存新的访问令牌
        saveUserToken(user, accessToken);

        // 构建认证响应对象
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // 将响应写入输出流
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}