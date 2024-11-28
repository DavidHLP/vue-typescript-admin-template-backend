package com.david.hlp.SpringBootWork.system.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色权限关联实体类，表示角色和权限的多对多关系。
 * 该类用于建立角色表（role）与权限表（permission）之间的关联。
 * 同时支持 JPA 和 MyBatis Plus。
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_permission")  // JPA 注解：对应数据库中的 role_permission 关联表
@TableName("role_permission")    // MyBatis Plus 注解：对应数据库中的 role_permission 关联表
public class RolePermission {

    /**
     * 主键（唯一标识符），用于标识此关联关系。
     * - 使用 JPA 的 @Id 和 @GeneratedValue 注解，主键生成策略为自增。
     * - 使用 MyBatis Plus 的 @TableId 注解，指定主键类型为自增。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO) // 主键自增
    private Long id;

    /**
     * 角色 ID，外键关联到角色表。
     * - 使用 JPA 的 @ManyToOne 注解，表示多对一的关系。
     * - 使用 @JoinColumn 指定外键列名为 role_id，引用 Role 实体的 id 列。
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id") // 外键关联到 Role 表
    private Role role;

    /**
     * 权限 ID，外键关联到权限表。
     * - 使用 JPA 的 @ManyToOne 注解，表示多对一的关系。
     * - 使用 @JoinColumn 指定外键列名为 permission_id，引用 Permission 实体的 id 列。
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", referencedColumnName = "id") // 外键关联到 Permission 表
    private Permission permission;
}