package com.david.hlp.SpringBootWork.system.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@EnableWebSecurity // 启用 Spring Security 功能
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
@EnableMethodSecurity // 启用方法级别的安全注解（例如 @PreAuthorize）
public class SecurityConfiguration {

    // 定义无需认证的 URL 白名单
    private static final String[] WHITE_LIST_URL = {
            "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", // Swagger 文档相关路径
            "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/api/v1/users/**","/api/v1/article/getArticleByStatusAndKeyWord"
    };

    // JWT 认证过滤器
    private final JwtAuthenticationFilter jwtAuthFilter;

    // 自定义认证提供器
    private final AuthenticationProvider authenticationProvider;

    // 自定义注销处理器
    private final LogoutHandler logoutHandler;

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
                // 1. 禁用 CSRF（因使用无状态 JWT 认证，无需 CSRF 防护）
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 配置请求授权规则
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll() // 白名单 URL 无需认证
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS 请求全局放行
                                .requestMatchers("/api/v1/user/manage/**").hasRole("ADMIN") // 用户管理接口需要 ADMIN 角色
                                .requestMatchers("/api/v1/articles/**").hasRole("ADMIN") // 文章管理接口需要 ADMIN 角色
                                .requestMatchers("/api/v1/pageviews/**").hasRole("ADMIN") // 页面浏览统计接口需要 ADMIN 角色
                                .requestMatchers("/api/v1/role/manage/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1//user/manage/**").hasAnyRole("ADMIN", "MANAGEMENT")
                                .requestMatchers("/api/v1/article/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll() // 静态资源放行
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // 其他静态资源放行
                                .anyRequest()
                                .authenticated() // 其他请求需认证
                )

                // 3. 配置会话管理策略为无状态（适配 JWT 的机制）
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                // 4. 设置自定义认证提供器
                .authenticationProvider(authenticationProvider)

                // 5. 在用户名密码认证过滤器之前添加自定义的 JWT 认证过滤器ya
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 6. 配置注销处理逻辑
                .logout(logout ->
                        logout.logoutUrl("/api/v1/users/logout") // 配置注销请求的 URL
                                .addLogoutHandler(logoutHandler) // 添加自定义注销处理器
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // 成功注销后清除安全上下文
                );

        return http.build(); // 构建并返回过滤器链
    }

    /**
     * 配置 CORS（跨域资源共享）策略。
     *
     * 允许所有来源、方法和请求头，支持跨域认证。
     *
     * @return 配置好的 CorsConfigurationSource。
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 允许所有来源（可根据需求限制特定来源）
        config.addAllowedMethod("*");        // 允许所有 HTTP 方法（如 GET, POST, PUT, DELETE）
        config.addAllowedHeader("*");        // 允许所有请求头
        config.setAllowCredentials(true);    // 允许发送凭证（如 Cookie）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 应用配置到所有路径

        return source;
    }
}