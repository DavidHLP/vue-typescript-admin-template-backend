package com.david.hlp.SpringBootWork.system.auth.entity;

import com.david.hlp.SpringBootWork.system.auth.token.Token;
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
 * 用户实体类，表示系统中的用户。
 * 实现了 Spring Security 的 UserDetails 接口，用于认证和授权。
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
   * 使用自动递增策略生成主键值。
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * 用户的姓名。
   */
  private String name;

  /**
   * 用户的头像 URL。
   */
  private String avatar;

  /**
   * 用户的个人简介。
   */
  private String introduction;

  /**
   * 用户的电子邮件地址，作为系统中唯一的用户名标识。
   * 使用 @Column 注解保证该字段在数据库中是唯一且不能为空。
   */
  @Column(unique = true, nullable = false)
  private String email;

  /**
   * 用户的密码，用于身份验证。
   */
  private String password;

  /**
   * 用户的角色。
   * 通过 @ManyToOne 建立多对一的关联关系，每个用户对应一个角色。
   * 使用 @JoinColumn 指定外键为 role_id，且 role 表中的主键为 id。
   * 默认使用 EAGER 加载策略，即在查询用户时自动加载角色信息。
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", referencedColumnName = "id")
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
   * 用户关联的令牌列表，用于支持多令牌场景。
   * 通过 @OneToMany 注解建立一对多的关联关系。
   * 使用 mappedBy 指定该关联关系由 Token 实体的 user 字段维护。
   * 通过 CascadeType.ALL 级联操作，使对用户的增删改操作会同步影响关联的 Token 数据。
   * orphanRemoval = true 确保删除用户时会移除所有孤立的 Token。
   */
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Token> tokens;

  /**
   * 获取用户的权限集合。
   * 权限集合由用户的角色中的权限信息决定。
   *
   * @return 权限集合，用于授权。
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  /**
   * 获取用户的密码。
   * 该方法由 Spring Security 使用。
   *
   * @return 用户的密码。
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * 获取用户的用户名（在此为电子邮件地址）。
   * 该方法由 Spring Security 使用。
   *
   * @return 用户的电子邮件地址。
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * 检查账户是否未过期。
   * 始终返回 true，表示账户未过期。
   *
   * @return true 表示账户未过期。
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 检查账户是否未锁定。
   * 始终返回 true，表示账户未锁定。
   *
   * @return true 表示账户未锁定。
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 检查凭证是否未过期。
   * 始终返回 true，表示凭证未过期。
   *
   * @return true 表示凭证未过期。
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 检查账户是否启用。
   * 始终返回 true，表示账户已启用。
   *
   * @return true 表示账户已启用。
   */
  @Override
  public boolean isEnabled() {
    return true;
  }
}