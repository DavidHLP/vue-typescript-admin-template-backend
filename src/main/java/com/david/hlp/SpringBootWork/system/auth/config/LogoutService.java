package com.david.hlp.SpringBootWork.system.auth.config;

import com.david.hlp.SpringBootWork.system.auth.token.TokenRepository;
import com.david.hlp.SpringBootWork.system.auth.util.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * 注销服务类。
 *
 * 该服务处理用户注销操作，将关联的 JWT 标记为失效并清除安全上下文。
 */
@Service
@RequiredArgsConstructor // 使用 Lombok 自动生成构造函数，注入依赖
public class LogoutService implements LogoutHandler {

  // 注入 TokenRepository，用于管理和查询令牌
  private final TokenRepository tokenRepository;
  private final RedisCache redisCache;

  /**
   * 处理用户注销逻辑。
   *
   * @param request        HTTP 请求对象，用于获取 Authorization 头中的 JWT。
   * @param response       HTTP 响应对象。
   * @param authentication 当前用户的认证信息。
   */
  @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
    // 获取请求头中的 Authorization 信息
    final String authHeader = request.getHeader("Authorization");
    final String jwt;

    // 如果 Authorization 头为空或不以 "Bearer " 开头，直接返回
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    // 提取 JWT（去除 "Bearer " 前缀）
    jwt = authHeader.substring(7);

    // 删除redis中的JWT
    redisCache.deleteObject(jwt);

    // 从数据库中查找存储的令牌
    var storedToken = tokenRepository.findByToken(jwt)
            .orElse(null);

    // 如果令牌存在，将其标记为失效（过期和撤销）
    if (storedToken != null) {
      storedToken.setExpired(true);  // 设置令牌过期
      storedToken.setRevoked(true); // 设置令牌撤销
      tokenRepository.save(storedToken); // 更新令牌状态到数据库

      // 清除 SecurityContext 中的认证信息
      SecurityContextHolder.clearContext();
    }
  }
}