package com.david.hlp.SpringBootWork.system.auth;

import com.david.hlp.SpringBootWork.system.service.imp.UserServiceImp;
import com.david.hlp.SpringBootWork.system.token.TokenRepository;
import com.david.hlp.SpringBootWork.system.util.RedisCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器。
 *
 * 该过滤器会在每次请求时运行一次，用于验证 JWT 并设置用户的认证信息到 Spring Security 的上下文中。
 */
@Component
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  // 用于处理 JWT 的服务类
  private final JwtService jwtService;

  // 用于加载用户详细信息
  private final UserDetailsService userDetailsService;

  // 用于检查 JWT 是否在数据库中有效
  private final TokenRepository tokenRepository;

  // 用户服务实现类，用于获取用户权限
  private final UserServiceImp userServiceImp;

  // Redis 缓存工具类，用于检查 JWT 是否存在于缓存中
  private final RedisCache redisCache;

  /**
   * 核心过滤逻辑。
   *
   * @param request     HTTP 请求对象
   * @param response    HTTP 响应对象
   * @param filterChain 过滤器链，用于继续执行后续过滤器
   * @throws ServletException 如果过滤过程中出现问题
   * @throws IOException      如果发生 I/O 错误
   */
  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    // 直接放行的接口路径
    if (request.getServletPath().equals("/api/v1/users/password")
            || request.getServletPath().equals("/api/v1/users/login")
            || request.getServletPath().equals("/api/v1/users/register")
            || request.getServletPath().startsWith("/api/v1/users/sendcode/")
            || request.getServletPath().equals("/api/v1/article/getArticleByStatusAndKeyWord")
    ) {
      filterChain.doFilter(request, response); // 放行请求到下一个过滤器
      return;
    }

    // 2. 从请求头中获取 Authorization 信息
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    // 如果 Authorization 头为空或者不以 "Bearer " 开头，直接放行
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // 提取 JWT（移除 "Bearer " 前缀）并解析用户邮箱
    jwt = authHeader.substring(7);
    userEmail = jwtService.extractUsername(jwt);

    // 检查 JWT 是否存在于 Redis 缓存中
    redisCache.hasKey(jwt);

    // 3. 验证用户邮箱和认证上下文
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // 从 UserDetailsService 加载用户信息
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

      // 检查 JWT 是否在数据库中有效，且未过期或撤销
      var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked()) // 检查令牌状态
              .orElse(false);

      // 验证 JWT 是否有效
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        // 创建用户认证令牌
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, // 用户详细信息
                null,        // 凭据为空
                userServiceImp.getAuthorities(userEmail) // 从 UserServiceImp 获取用户权限
        );

        // 设置认证请求的详细信息
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // 将认证信息设置到 Spring Security 的上下文
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // 4. 放行请求到过滤器链的下一个过滤器
    filterChain.doFilter(request, response);
  }
}