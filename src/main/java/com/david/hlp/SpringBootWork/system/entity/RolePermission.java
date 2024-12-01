package com.david.hlp.SpringBootWork.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色权限关联实体类。
 *
 * 描述：
 * <p>
 * - 表示角色和权限之间的多对多关联关系。
 * <p>
 * - 通过中间表 `role_permission` 连接角色表（role）和权限表（permission）。
 * <p>
 * - 支持 JPA 和 MyBatis Plus，同时兼容两种框架的数据库操作。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 *   - @Builder 提供构建器模式，用于灵活创建对象实例。
 *   - @NoArgsConstructor 自动生成无参构造函数。
 *   - @AllArgsConstructor 自动生成全参构造函数。
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
     * 主键（唯一标识符）。
     *
     * 描述：
     * <p>
     * - 表示此角色权限关联的唯一标识符。
     * <p>
     * - 使用 JPA 的 @Id 和 @GeneratedValue 注解，主键生成策略为自增。
     * <p>
     * - 使用 MyBatis Plus 的 @TableId 注解，指定主键类型为自增。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA 主键自增策略
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO) // MyBatis Plus 主键自增
    private Long id;

    /**
     * 角色 ID。
     *
     * 描述：
     * <p>
     * - 外键关联到角色表（role）的主键 `id`。
     * <p>
     * - 表示当前关联关系中的角色信息。
     * <p>
     * - 使用 JPA 的 @ManyToOne 注解定义多对一关系。
     * <p>
     * - 使用 @JoinColumn 注解指定外键列名为 `role_id`。
     *
     * 配置：
     * <p>
     * - fetch = FetchType.EAGER：在加载关联数据时立即加载角色信息。
     * <p>
     * - @JoinColumn：
     *   - name = "role_id"：关联表中的外键列名。
     *   - referencedColumnName = "id"：引用角色表中的主键列名。
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id") // 外键关联到 Role 表
    private Role role;

    /**
     * 权限 ID。
     *
     * 描述：
     * <p>
     * - 外键关联到权限表（permission）的主键 `id`。
     * <p>
     * - 表示当前关联关系中的权限信息。
     * <p>
     * - 使用 JPA 的 @ManyToOne 注解定义多对一关系。
     * <p>
     * - 使用 @JoinColumn 注解指定外键列名为 `permission_id`。
     *
     * 配置：
     * <p>
     * - fetch = FetchType.EAGER：在加载关联数据时立即加载权限信息。
     * <p>
     * - @JoinColumn：
     *   - name = "permission_id"：关联表中的外键列名。
     *   - referencedColumnName = "id"：引用权限表中的主键列名。
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", referencedColumnName = "id") // 外键关联到 Permission 表
    private Permission permission;
}