package com.david.hlp.SpringBootWork.system.auth.enumentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 定义用户权限的枚举类。
 * 每个枚举值表示一种权限操作，并绑定对应的权限标识符字符串。
 */
@RequiredArgsConstructor
public enum DefaultRolePermission {

    /**
     * 管理员权限：读取。
     */
    ADMIN_READ("admin:read"),

    /**
     * 管理员权限：更新。
     */
    ADMIN_UPDATE("admin:update"),

    /**
     * 管理员权限：创建。
     */
    ADMIN_CREATE("admin:create"),

    /**
     * 管理员权限：删除。
     */
    ADMIN_DELETE("admin:delete"),

    /**
     * 管理员权限：读取管理模块信息。
     */
    MANAGER_READ("management:read"),

    /**
     * 管理员权限：更新管理模块信息。
     */
    MANAGER_UPDATE("management:update"),

    /**
     * 管理员权限：创建管理模块信息。
     */
    MANAGER_CREATE("management:create"),

    /**
     * 管理员权限：删除管理模块信息。
     */
    MANAGER_DELETE("management:delete");

    /**
     * 权限对应的标识符字符串。
     */
    @Getter
    private final String permission;
}
