package com.david.hlp.SpringBootWork.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限实体类，表示系统中的权限信息。
 * 该类同时使用 JPA 和 MyBatis Plus 注解，以便支持两种框架的操作。
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
     * - 使用 JPA 的 @Id 和 @GeneratedValue 注解，指定主键生成策略为自增。
     * - 使用 MyBatis Plus 的 @TableId 注解，指定主键类型为自增。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA 主键自增策略
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO) // MyBatis Plus 主键自增
    private Long id;  // 主键

    /**
     * 权限标识符。
     * - 表示具体的权限操作，如 "admin:read", "admin:update", "user:delete" 等。
     * - 使用 JPA 的 @Column 注解，确保该字段在数据库中唯一且不能为空。
     */
    @Column(unique = true, nullable = false) // JPA 注解：限制该字段唯一且不能为空
    private String permission;  // 权限标识符

    /**
     * 权限的描述信息（可选字段）。
     * - 用于对权限进行详细说明，方便管理员或开发者理解权限的含义。
     */
    private String description;  // 权限描述
}