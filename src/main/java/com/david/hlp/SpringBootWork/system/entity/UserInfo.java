package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息类。
 *
 * 描述：
 * <p>
 * - 用于封装用户的基本信息和角色信息。
 * <p>
 * - 通常用于前端展示用户资料或在系统中传递用户信息。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 *   - @Builder 提供构建器模式，用于灵活创建对象实例。
 *   - @NoArgsConstructor 自动生成无参构造函数。
 *   - @AllArgsConstructor 自动生成全参构造函数。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    /**
     * 用户的姓名。
     *
     * 描述：
     * - 表示用户的真实姓名或显示名称。
     */
    private String name;

    /**
     * 用户的头像 URL。
     *
     * 描述：
     * - 指向用户头像的存储位置，通常是一个 URL。
     */
    private String avatar;

    /**
     * 用户的个人简介。
     *
     * 描述：
     * - 用户的个性化描述或简要信息。
     */
    private String introduction;

    /**
     * 用户的电子邮件地址。
     *
     * 描述：
     * - 用于标识用户的唯一标识符。
     * - 在系统中通常作为用户名或联系方式。
     */
    private String email;

    /**
     * 用户的角色信息。
     *
     * 描述：
     * - 表示用户在系统中的角色。
     * - 包含角色的名称和权限集合等信息。
     */
    private Role roles;
}