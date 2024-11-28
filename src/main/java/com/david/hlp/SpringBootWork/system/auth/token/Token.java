package com.david.hlp.SpringBootWork.system.auth.token;

import com.david.hlp.SpringBootWork.system.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token 实体类。
 *
 * 用于存储与用户相关的认证令牌（如 JWT）。
 * 包括令牌内容、类型、状态（是否过期/撤销）以及关联的用户信息。
 */
@Data // 自动生成 Getter、Setter、toString、equals 和 hashCode 方法
@Builder // 提供构建器模式，用于灵活创建对象
@NoArgsConstructor // 自动生成无参构造函数
@AllArgsConstructor // 自动生成全参构造函数
@Entity // 标识该类为 JPA 实体类
public class Token {

  /**
   * 主键 ID，自动生成。
   */
  @Id
  @GeneratedValue
  public Integer id;

  /**
   * 令牌内容，必须唯一。
   */
  @Column(unique = true) // 设置该列的值必须唯一
  public String token;

  /**
   * 令牌类型。
   * 使用枚举定义，如 BEARER。
   */
  @Enumerated(EnumType.STRING) // 将枚举值存储为字符串
  public TokenType tokenType = TokenType.BEARER; // 默认类型为 BEARER

  /**
   * 标识令牌是否已被撤销。
   */
  public boolean revoked;

  /**
   * 标识令牌是否已过期。
   */
  public boolean expired;

  /**
   * 关联的用户。
   * 每个令牌属于一个用户，使用多对一关系映射。
   */
  @ManyToOne(fetch = FetchType.LAZY) // 多对一关系，懒加载用户信息
  @JoinColumn(name = "user_id") // 数据库中外键列名为 user_id
  public User user;
}
