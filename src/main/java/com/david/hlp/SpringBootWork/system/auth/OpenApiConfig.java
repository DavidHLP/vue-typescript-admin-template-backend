package com.david.hlp.SpringBootWork.system.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * 配置 OpenAPI (Swagger) 文档信息。
 *
 * 包括 API 基本信息、服务器环境和安全设置。
 * 此配置使用 OpenAPI 3 的注解。
 */
@OpenAPIDefinition(
        // 定义 API 的基本信息
        info = @Info(
                contact = @Contact(
                        name = "Alibou", // 联系人名称
                        email = "contact@aliboucoding.com", // 联系人邮箱
                        url = "https://aliboucoding.com/course" // 联系人网址
                ),
                description = "OpenApi documentation for Spring Security", // API 描述
                title = "OpenApi specification - Alibou", // API 标题
                version = "1.0", // API 版本号
                license = @License(
                        name = "Licence name", // 许可证名称
                        url = "https://some-url.com" // 许可证链接
                ),
                termsOfService = "Terms of service" // 服务条款
        ),
        // 定义 API 的服务器环境
        servers = {
                @Server(
                        description = "Local ENV", // 本地环境描述
                        url = "http://localhost:8080" // 本地环境地址
                ),
                @Server(
                        description = "PROD ENV", // 生产环境描述
                        url = "https://aliboucoding.com/course" // 生产环境地址
                )
        },
        // 定义 API 的安全要求
        security = {
                @SecurityRequirement(
                        name = "bearerAuth" // 引用的安全方案名称
                )
        }
)
// 定义 API 的安全方案
@SecurityScheme(
        name = "bearerAuth", // 安全方案的名称，需与 @SecurityRequirement 中一致
        description = "JWT auth description", // 安全方案的描述
        scheme = "bearer", // 认证模式为 Bearer Token
        type = SecuritySchemeType.HTTP, // HTTP 类型的安全方案
        bearerFormat = "JWT", // Bearer Token 的格式
        in = SecuritySchemeIn.HEADER // Token 放置在 HTTP 请求头中
)
public class OpenApiConfig {
    // 配置类无需额外代码，通过注解定义 OpenAPI 文档内容
}