package com.david.hlp.SpringBootWork.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色实体类。
 *
 * 描述：
 * <p>
 * - 表示系统中的角色，每个角色可以对应多个权限。
 * <p>
 * - 使用 JPA 和 MyBatis Plus 注解，支持两种框架的数据库操作。
 * <p>
 * - 角色与权限之间为多对多关系，通过中间表 role_permission 关联。
 * <p>
 * - 提供方法将角色及其权限映射为 Spring Security 的权限集合。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 *   - @Builder 提供构建器模式，用于灵活创建对象实例。
 *   - @NoArgsConstructor 自动生成无参构造函数。
 *   - @AllArgsConstructor 自动生成全参构造函数。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")  // JPA 注解，对应数据库中的 role 表
@TableName("role")  // MyBatis Plus 注解，对应数据库中的 role 表
public class Role {

    /**
     * 角色的唯一标识符（主键）。
     *
     * 描述：
     * <p>
     * - 该字段为角色表的主键，用于唯一标识一条角色记录。
     * <p>
     * - 使用 JPA 的 @Id 和 @GeneratedValue 注解指定主键生成策略为自增。
     * <p>
     * - 使用 MyBatis Plus 的 @TableId 注解，指定主键类型为自增。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO) // MyBatis Plus 主键自增
    private Long id;  // 主键

    /**
     * 角色名称。
     *
     * 描述：
     * <p>
     * - 表示角色的唯一标识名称，例如：USER、ADMIN、MANAGER 等。
     * <p>
     * - 使用 JPA 的 @Column 注解，确保 roleName 在数据库中唯一且不能为空。
     */
    @Column(unique = true, nullable = false)
    private String roleName;  // 角色名称

    /**
     * 角色对应的权限集合。
     *
     * 描述：
     * <p>
     * - 使用 JPA 的 @ManyToMany 注解定义多对多关联关系。
     * <p>
     * - 通过中间表 role_permission 关联 role 表和 permission 表。
     * <p>
     * - 配置说明：
     *   - fetch = FetchType.EAGER：查询 Role 时立即加载关联的权限集合。
     *   - @JoinTable 指定中间表的名称及外键字段：
     *     - joinColumns：当前实体 Role 的外键列名。
     *     - inverseJoinColumns：关联实体 Permission 的外键列名。
     */
    @ManyToMany(fetch = FetchType.EAGER) // 立即加载关联的权限集合
    @JoinTable(
            name = "role_permission",  // 中间表名称
            joinColumns = @JoinColumn(name = "role_id"),  // 当前实体 Role 的外键
            inverseJoinColumns = @JoinColumn(name = "permission_id")  // 关联实体 Permission 的外键
    )
    private List<Permission> permissions;  // 角色对应的权限集合

    /**
     * 获取角色对应的权限集合，用于 Spring Security 的授权功能。
     *
     * 描述：
     * <p>
     * - 将角色及其关联的权限转换为 Spring Security 的 SimpleGrantedAuthority 集合。
     * <p>
     * - 如果角色未关联任何权限，仅返回角色标识符（以 "ROLE_" 为前缀）。
     * <p>
     * - 返回结果可直接用于 Spring Security 的权限验证和授权。
     *
     * @return 包含角色标识和权限的集合 (List<SimpleGrantedAuthority>)。
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        // 如果权限集合为空，返回角色标识符
        if (permissions == null || permissions.isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
        }

        // 将权限集合转换为 SimpleGrantedAuthority 集合
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))  // 转换权限
                .collect(Collectors.toList());

        // 添加角色标识符（ROLE_前缀）
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        return authorities;
    }
}