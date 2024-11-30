package com.david.hlp.SpringBootWork.system.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器类。
 *
 * 提供管理员相关的 RESTful API，使用 Spring Security 进行权限控制。
 * 只有具有 ADMIN 角色的用户才能访问该控制器。
 */
@RestController
@RequestMapping("/api/v1/admin") // 定义基础路由为 /api/v1/admin
@PreAuthorize("hasRole('ADMIN')") // 全局权限控制：只有 ADMIN 角色的用户可以访问该控制器
public class AdminController {

    /**
     * 处理 GET 请求。
     *
     * @return 返回管理员控制器的 GET 信息。
     */
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')") // 仅具有 admin:read 权限的用户可以访问
    public String get() {
        return "GET:: admin controller";
    }

    /**
     * 处理 POST 请求。
     *
     * @return 返回管理员控制器的 POST 信息。
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
     * @return 返回管理员控制器的 PUT 信息。
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
     * @return 返回管理员控制器的 DELETE 信息。
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')") // 仅具有 admin:delete 权限的用户可以访问
    @Hidden // 隐藏该接口，不在 Swagger 文档中显示
    public String delete() {
        return "DELETE:: admin controller";
    }
}