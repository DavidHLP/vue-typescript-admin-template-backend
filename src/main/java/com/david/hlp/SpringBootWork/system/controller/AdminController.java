package com.david.hlp.SpringBootWork.system.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器类。
 *
 * 描述：
 * <p>
 * - 提供与管理员相关的 RESTful API 接口。
 * <p>
 * - 使用 Spring Security 进行权限控制，确保只有具有 ADMIN 角色的用户可以访问。
 * <p>
 * - 通过方法级别的权限控制，细化不同操作的权限要求。
 * <p>
 * - 使用 Swagger 的 @Hidden 注解隐藏部分接口，不显示在 API 文档中。
 */
@RestController
@RequestMapping("/api/v1/admin") // 定义基础路由为 /api/v1/admin
@PreAuthorize("hasRole('ADMIN')") // 全局权限控制：只有 ADMIN 角色的用户可以访问该控制器
public class AdminController {

    /**
     * 处理 GET 请求。
     *
     * 描述：
     * <p>
     * - 返回管理员控制器的 GET 信息。
     * <p>
     * - 仅具有 `admin:read` 权限的用户可以访问。
     *
     * 权限控制：
     * <p>
     * - @PreAuthorize("hasAuthority('admin:read')")：用户必须具备 `admin:read` 权限。
     *
     * @return 返回字符串 "GET:: admin controller"。
     */
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')") // 仅具有 admin:read 权限的用户可以访问
    public String get() {
        return "GET:: admin controller";
    }

    /**
     * 处理 POST 请求。
     *
     * 描述：
     * <p>
     * - 返回管理员控制器的 POST 信息。
     * <p>
     * - 仅具有 `admin:create` 权限的用户可以访问。
     *
     * 权限控制：
     * <p>
     * - @PreAuthorize("hasAuthority('admin:create')")：用户必须具备 `admin:create` 权限。
     *
     * Swagger 配置：
     * <p>
     * - 使用 @Hidden 注解隐藏该接口，不在 Swagger 文档中显示。
     *
     * @return 返回字符串 "POST:: admin controller"。
     */
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')") // 仅具有 admin:create 权限的用户可以访问
    @Hidden // 隐藏该接口，不在 Swagger 文档中显示
    public String post() {
        return "POST:: admin controller";
    }

    /**
     * 处理 PUT 请求。
     *
     * 描述：
     * <p>
     * - 返回管理员控制器的 PUT 信息。
     * <p>
     * - 仅具有 `admin:update` 权限的用户可以访问。
     *
     * 权限控制：
     * <p>
     * - @PreAuthorize("hasAuthority('admin:update')")：用户必须具备 `admin:update` 权限。
     *
     * Swagger 配置：
     * <p>
     * - 使用 @Hidden 注解隐藏该接口，不在 Swagger 文档中显示。
     *
     * @return 返回字符串 "PUT:: admin controller"。
     */
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')") // 仅具有 admin:update 权限的用户可以访问
    @Hidden // 隐藏该接口，不在 Swagger 文档中显示
    public String put() {
        return "PUT:: admin controller";
    }

    /**
     * 处理 DELETE 请求。
     *
     * 描述：
     * <p>
     * - 返回管理员控制器的 DELETE 信息。
     * <p>
     * - 仅具有 `admin:delete` 权限的用户可以访问。
     *
     * 权限控制：
     * <p>
     * - @PreAuthorize("hasAuthority('admin:delete')")：用户必须具备 `admin:delete` 权限。
     *
     * Swagger 配置：
     * <p>
     * - 使用 @Hidden 注解隐藏该接口，不在 Swagger 文档中显示。
     *
     * @return 返回字符串 "DELETE:: admin controller"。
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')") // 仅具有 admin:delete 权限的用户可以访问
    @Hidden // 隐藏该接口，不在 Swagger 文档中显示
    public String delete() {
        return "DELETE:: admin controller";
    }
}