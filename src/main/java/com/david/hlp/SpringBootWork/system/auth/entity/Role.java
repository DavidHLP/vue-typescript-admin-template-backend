package com.david.hlp.SpringBootWork.system.auth.entity;

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
 * 角色实体类，表示系统中的角色。
 * 角色和权限之间建立了多对多关系。
 * 该类同时使用 JPA 和 MyBatis Plus 注解。
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
     * 使用 JPA 的 @Id 和 @GeneratedValue 注解，指定主键生成策略为自增。
     * 使用 MyBatis Plus 的 @TableId 注解，指定主键类型为自增。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO) // MyBatis Plus 注解
    private Long id;  // 主键

    /**
     * 角色名称。
     * 例如：USER、ADMIN、MANAGER 等。
     * 使用 JPA 的 @Column 注解，确保 roleName 在数据库中唯一且不能为空。
     */
    @Column(unique = true, nullable = false)
    private String roleName;  // 角色名称

    /**
     * 角色对应的权限集合。
     * 使用 JPA 的 @ManyToMany 注解建立多对多关联。
     * 使用 @JoinTable 指定关联表 role_permission 及其外键列。
     *
     * - fetch = FetchType.EAGER：在查询 Role 时立即加载关联的权限集合。
     * - joinColumns：指定当前实体 Role 的外键为 role_id。
     * - inverseJoinColumns：指定关联实体 Permission 的外键为 permission_id。
     */
    @ManyToMany(fetch = FetchType.EAGER) // 修改为 EAGER，立即加载关联数据
    @JoinTable(
            name = "role_permission",  // 关联表名称
            joinColumns = @JoinColumn(name = "role_id"),  // 当前实体 Role 的外键
            inverseJoinColumns = @JoinColumn(name = "permission_id")  // 关联实体 Permission 的外键
    )
    private List<Permission> permissions;  // 角色对应的权限集合

    /**
     * 获取角色对应的权限集合，用于 Spring Security 的授权功能。
     *
     * @return 权限集合，包含角色标识和具体权限。
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
