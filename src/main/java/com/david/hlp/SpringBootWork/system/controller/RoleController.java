package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.service.imp.RoleServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器类。
 *
 * 描述：
 * <p>
 * - 提供与角色管理相关的 RESTful API 接口。
 * <p>
 * - 主要处理与角色相关的操作，如获取角色列表、创建角色、更新角色、删除角色等功能。
 * <p>
 * - 使用 Spring MVC 注解实现控制器功能。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @RequiredArgsConstructor 自动生成构造函数，注入 `RoleServiceImp` 依赖。
 */
@RestController
@RequestMapping("/api/v1/role") // 将控制器映射到 /api/v1/role 路径
@RequiredArgsConstructor // 自动生成构造函数，注入必要的依赖
public class RoleController {

    /**
     * 角色服务实现类。
     *
     * 描述：
     * <p>
     * - 注入 `RoleServiceImp`，用于处理与角色相关的具体业务逻辑。
     * <p>
     * - 使用 `final` 确保依赖在类初始化时注入，并且不可更改。
     */
    private final RoleServiceImp roleServiceImp;
}
