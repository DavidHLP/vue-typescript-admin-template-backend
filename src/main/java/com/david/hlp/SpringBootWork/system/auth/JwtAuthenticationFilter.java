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

  private final UserServiceImp userServiceImp;

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
    // 如果请求路径是认证相关的接口，直接放行，不进行 JWT 验证
    if (request.getServletPath().equals("/api/v1/users/login")
            || request.getServletPath().equals("/api/v1/users/register")
            || request.getServletPath().equals("/api/v1/articles")
            || request.getServletPath().equals("/api/v1/pageviews")
    ) {
      filterChain.doFilter(request, response);
      return;
    }

    // 从请求头中获取 Authorization 信息
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;

    // 如果 Authorization 头为空或者不以 "Bearer " 开头，直接放行
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // 提取 JWT 和用户邮箱
    jwt = authHeader.substring(7); // 移除 "Bearer " 前缀
    userEmail = jwtService.extractUsername(jwt);

    // JWT还存在Redis中
    redisCache.hasKey(jwt);

    // 如果用户邮箱不为空，且当前上下文中没有已认证的用户
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // 从 UserDetailsService 加载用户信息
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

      // 检查 JWT 是否有效且未过期或撤销
      var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked()) // 检查令牌的状态
              .orElse(false);

      // 如果令牌有效，设置用户认证信息到 SecurityContext 中
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, // 用户详情
                null,        // 无需凭据
                userServiceImp.getAuthorities(userEmail) // 用户权限
        );

        // 设置认证请求的详细信息
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // 将认证信息设置到 Spring Security 上下文
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // 继续执行过滤器链
    filterChain.doFilter(request, response);
  }
}