package com.david.hlp.SpringBootWork.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限实体类。
 *
 * 描述：
 * <p>
 * - 表示系统中的权限信息，包括权限标识符和描述。
 * <p>
 * - 同时使用 JPA 和 MyBatis Plus 注解，支持两种框架的数据库操作。
 * <p>
 * - 包括权限的主键 (id)、权限标识符 (permission)、权限描述 (description)。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 *   - @NoArgsConstructor 自动生成无参构造函数。
 *   - @AllArgsConstructor 自动生成全参构造函数。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")  // JPA 注解：对应数据库中的 permission 表
@TableName("permission") // MyBatis Plus 注解：对应数据库中的 permission 表
public class Permission {

    /**
     * 权限的唯一标识符（主键）。
     *
     * 描述：
     * <p>
     * - 该字段为权限表的主键，用于唯一标识一条权限记录。
     * <p>
     * - 使用 JPA 的 @Id 和 @GeneratedValue 注解，指定主键生成策略为自增。
     * <p>
     * - 使用 MyBatis Plus 的 @TableId 注解，指定主键类型为自增。
     *
     * JPA 注解：
     * - @Id 标识该字段为主键。
     * - @GeneratedValue 指定主键生成策略为自增。
     *
     * MyBatis Plus 注解：
     * - @TableId 指定该字段为主键，并指定主键生成策略。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA 主键自增策略
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO) // MyBatis Plus 主键自增
    private Long id;  // 主键

    /**
     * 权限标识符。
     *
     * 描述：
     * <p>
     * - 表示具体的权限操作，例如 "admin:read", "admin:update", "user:delete" 等。
     * <p>
     * - 用于在权限校验和授权时标识权限。
     * <p>
     * - 使用 JPA 的 @Column 注解，确保该字段在数据库中唯一且不能为空。
     *
     * JPA 注解：
     * - @Column(unique = true, nullable = false) 限制该字段在数据库中唯一且不能为空。
     */
    @Column(unique = true, nullable = false) // JPA 注解：限制该字段唯一且不能为空
    private String permission;  // 权限标识符

    /**
     * 权限的描述信息（可选字段）。
     *
     * 描述：
     * <p>
     * - 对权限进行详细说明，帮助管理员或开发者理解权限的功能和范围。
     * <p>
     * - 该字段为可选字段，可以为空。
     */
    private String description;  // 权限描述
}