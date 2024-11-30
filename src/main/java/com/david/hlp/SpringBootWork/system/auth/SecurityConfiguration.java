package com.david.hlp.SpringBootWork.system.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Spring Security 配置类。
 *
 * 配置安全过滤器链、认证授权规则以及会话管理策略。
 */
@Configuration
@EnableWebSecurity // 启用 Spring Security
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
@EnableMethodSecurity // 启用方法级别的安全注解（如 @PreAuthorize）
public class SecurityConfiguration {

    // 定义不需要认证的白名单 URL
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/**",
            "/api/v1/auth/**", // 认证相关接口
            "/api/v1/demo/**",
            "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", // Swagger 文档
            "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter jwtAuthFilter; // JWT 认证过滤器
    private final AuthenticationProvider authenticationProvider; // 自定义认证提供器
    private final LogoutHandler logoutHandler; // 自定义注销处理器

    /**
     * 配置安全过滤器链。
     *
     * @param http HttpSecurity 对象，用于配置安全规则。
     * @return 配置后的 SecurityFilterChain。
     * @throws Exception 如果配置失败。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF 防护（因使用无状态认证机制）
                .csrf(AbstractHttpConfigurer::disable)

                // 配置请求授权规则
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll() // 白名单 URL 无需认证
//                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGEMENT.name()) // 管理接口需要 ADMIN 或 MANAGER 角色
//                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name()) // GET 请求需具备读取权限
//                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name()) // POST 请求需具备创建权限
//                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name()) // PUT 请求需具备更新权限
//                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name()) // DELETE 请求需具备删除权限
                                .anyRequest()
                                .authenticated() // 其他请求需认证
                )

                // 配置会话管理策略为无状态（JWT）
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                // 设置自定义认证提供器
                .authenticationProvider(authenticationProvider)

                // 在用户名密码认证过滤器之前添加 JWT 认证过滤器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 配置注销处理
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout") // 注销 URL
                                .addLogoutHandler(logoutHandler) // 自定义注销处理器
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // 注销成功后清除安全上下文
                );

        return http.build(); // 构建并返回过滤器链
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 允许所有来源
        config.addAllowedMethod("*");        // 允许所有 HTTP 方法
        config.addAllowedHeader("*");        // 允许所有 Header
        config.setAllowCredentials(true);    // 允许携带凭证

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}