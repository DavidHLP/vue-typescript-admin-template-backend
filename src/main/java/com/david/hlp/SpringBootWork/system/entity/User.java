package com.david.hlp.SpringBootWork.system.entity;

import com.david.hlp.SpringBootWork.system.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用户实体类。
 *
 * 描述：
 * <p>
 * - 表示系统中的用户信息。
 * <p>
 * - 实现了 Spring Security 的 `UserDetails` 接口，用于用户的认证和授权。
 * <p>
 * - 包含用户的基本信息（如姓名、邮箱、密码）以及角色和令牌的关联关系。
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
@Table(name = "user") // 指定数据库中的表名为 user
public class User implements UserDetails {

  /**
   * 用户的唯一标识符（主键）。
   *
   * 描述：
   * <p>
   * - 使用自动递增策略生成主键值。
   * <p>
   * - 标识数据库中用户记录的唯一标识符。
   */
  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 用户的姓名。
   */
  @Column(nullable = false)
  private String name;

  /**
   * 用户的头像 URL。
   *
   * 描述：
   * - 表示用户头像的存储位置（可以是 URL 路径）。
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
   * - 作为系统中唯一的用户名标识。
   * <p>
   * - 使用 @Column 注解保证该字段在数据库中是唯一且不能为空。
   */
  @Column(unique = true, nullable = false)
  private String email;

  /**
   * 用户的密码。
   *
   * 描述：
   * - 用于身份验证。
   * - 在存储前应进行加密处理。
   */
  @Column(nullable = false)
  private String password;

  /**
   * 用户的角色。
   *
   * 描述：
   * - 通过 @ManyToOne 建立多对一的关联关系，每个用户对应一个角色。
   * - 默认使用 EAGER 加载策略，即在查询用户时自动加载角色信息。
   *
   * 配置：
   * <p>
   * - @JoinColumn 指定外键为 role_id，且 role 表中的主键为 id。
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", referencedColumnName = "id" , nullable = false)
  private Role role;

  /**
   * 返回用户的角色名称。
   *
   * @return 角色名称。
   */
  public String getRole() {
    return role.getRoleName();
  }

  /**
   * 用户关联的令牌列表。
   *
   * 描述：
   * <p>
   * - 支持多令牌场景，表示用户当前活跃的认证令牌。
   * <p>
   * - 通过 @OneToMany 注解建立一对多的关联关系。
   * <p>
   * - 使用 `mappedBy` 指定该关联关系由 Token 实体的 `user` 字段维护。
   * <p>
   * - 使用 `CascadeType.ALL` 实现级联操作，对用户的增删改操作会同步影响关联的 Token 数据。
   * <p>
   * - orphanRemoval = true 确保删除用户时会移除所有孤立的 Token。
   */
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Token> tokens;

  @Column(columnDefinition = "BOOLEAN DEFAULT true", nullable = false)
  @Builder.Default
  private Boolean status = Boolean.TRUE;

  // Spring Security UserDetails 接口方法实现

  /**
   * 获取用户的权限集合。
   *
   * 描述：
   * - 权限集合由用户的角色中的权限信息决定。
   *
   * @return 权限集合，用于授权。
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  /**
   * 获取用户的密码。
   *
   * @return 用户的密码。
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * 获取用户的用户名。
   *
   * 描述：
   * - 在此为电子邮件地址。
   *
   * @return 用户的电子邮件地址。
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * 检查账户是否未过期。
   *
   * 描述：
   * - 始终返回 true，表示账户未过期。
   *
   * @return true 表示账户未过期。
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 检查账户是否未锁定。
   *
   * 描述：
   * - 始终返回 true，表示账户未锁定。
   *
   * @return true 表示账户未锁定。
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 检查凭证是否未过期。
   *
   * 描述：
   * - 始终返回 true，表示凭证未过期。
   *
   * @return true 表示凭证未过期。
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 检查账户是否启用。
   *
   * 描述：
   * - 始终返回 true，表示账户已启用。
   *
   * @return true 表示账户已启用。
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}