package com.david.hlp.SpringBootWork.system.enumentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户权限枚举类。
 *
 * 描述：
 * <p>
 * - 定义系统中的默认权限操作类型。
 * <p>
 * - 每个枚举值表示一种具体的权限操作，绑定到一个权限标识符字符串。
 * <p>
 * - 使用权限标识符字符串实现权限控制，与系统中的角色、资源进行绑定。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Getter 自动生成 `permission` 字段的 Getter 方法。
 *   - @RequiredArgsConstructor 自动生成带 `final` 字段的构造函数。
 * <p>
 */
@RequiredArgsConstructor
public enum DefaultRolePermission {

    /**
     * 管理员权限：读取。
     *
     * 描述：
     * - 表示管理员可以读取相关信息。
     */
    ADMIN_READ("admin:read"),

    /**
     * 管理员权限：更新。
     *
     * 描述：
     * - 表示管理员可以更新相关信息。
     */
    ADMIN_UPDATE("admin:update"),

    /**
     * 管理员权限：创建。
     *
     * 描述：
     * - 表示管理员可以创建相关信息。
     */
    ADMIN_CREATE("admin:create"),

    /**
     * 管理员权限：删除。
     *
     * 描述：
     * - 表示管理员可以删除相关信息。
     */
    ADMIN_DELETE("admin:delete"),

    /**
     * 管理模块权限：读取。
     *
     * 描述：
     * - 表示管理员可以读取管理模块的信息。
     */
    MANAGER_READ("management:read"),

    /**
     * 管理模块权限：更新。
     *
     * 描述：
     * - 表示管理员可以更新管理模块的信息。
     */
    MANAGER_UPDATE("management:update"),

    /**
     * 管理模块权限：创建。
     *
     * 描述：
     * - 表示管理员可以创建管理模块的信息。
     */
    MANAGER_CREATE("management:create"),

    /**
     * 管理模块权限：删除。
     *
     * 描述：
     * - 表示管理员可以删除管理模块的信息。
     */
    MANAGER_DELETE("management:delete");

    /**
     * 权限对应的标识符字符串。
     *
     * 描述：
     * - 用于标识具体的权限操作。
     * - 通常存储在数据库中，与角色和用户权限进行匹配。
     */
    @Getter
    private final String permission; // 使用 final 确保权限标识符不可修改
}