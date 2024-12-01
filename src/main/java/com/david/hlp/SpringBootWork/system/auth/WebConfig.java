package com.david.hlp.SpringBootWork.system.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局 CORS 配置类。
 *
 * 配置跨域资源共享（CORS）规则，用于处理前后端分离架构中的跨域请求。
 */
@Configuration
public class WebConfig {

    /**
     * 定义一个全局的 CORS 过滤器。
     *
     * 配置允许的来源、HTTP 方法、请求头和是否携带凭证。
     *
     * @return 配置好的 CorsFilter 实例。
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有来源（可根据实际需求限制特定来源）
        config.addAllowedOriginPattern("*");
        // 允许所有 HTTP 方法（GET, POST, PUT, DELETE 等）
        config.addAllowedMethod("*");
        // 允许所有请求头（如 Content-Type, Authorization 等）
        config.addAllowedHeader("*");
        // 允许客户端发送凭证（如 Cookie）
        config.setAllowCredentials(true);

        // 定义 URL 基础的 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 将配置应用到所有路径
        source.registerCorsConfiguration("/**", config);

        // 返回配置好的 CORS 过滤器实例
        return new CorsFilter(source);
    }
}