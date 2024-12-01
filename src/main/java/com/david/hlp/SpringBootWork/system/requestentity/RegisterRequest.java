package com.david.hlp.SpringBootWork.system.requestentity;

import com.david.hlp.SpringBootWork.system.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求数据类。
 * <p>
 * 用于封装用户注册操作中提交的必要信息。
 * <p>
 * 包括用户的基本信息（如名称、邮箱、密码等）以及附加信息（如角色和验证码）。
 * <p>
 * 使用 Lombok 注解简化代码：
 * <p>
 * - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 * <p>
 * - @Builder 提供构建器模式，便于灵活创建对象实例。
 * <p>
 * - @AllArgsConstructor 自动生成包含所有字段的构造函数。
 * <p>
 * - @NoArgsConstructor 自动生成无参构造函数。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  /**
   * 用户的名字。
   * <p>
   * 描述：
   * <p>
   * - 用户的显示名称或实际姓名。
   * <p>
   * - 在用户提交注册时作为必填字段之一。
   */
  private String name;

  /**
   * 用户的邮箱。
   * <p>
   * 描述：
   * <p>
   * - 用户的唯一标识，用于登录和注册验证。
   * <p>
   * - 通常会用于发送验证码或通知邮件。
   */
  private String email;

  /**
   * 用户的密码。
   * <p>
   * 描述：
   * <p>
   * - 用户的敏感信息，用于身份验证。
   * <p>
   * - 在存储之前会通过服务端进行加密处理。
   */
  private String password;

  /**
   * 用户的角色。
   * <p>
   * 描述：
   * <p>
   * - 指定用户的权限级别（如管理员、普通用户等）。
   * <p>
   * - 角色信息可影响用户在系统中的访问权限和功能操作范围。
   */
  private Role role;

  /**
   * 用户名。
   * <p>
   * 描述：
   * <p>
   * - 用户的登录名，可以是字母、数字或其他组合。
   * <p>
   * - 如果设置此字段，会同步更新 `name` 字段。
   */
  private String username;

  /**
   * 验证码。
   * <p>
   * 描述：
   * <p>
   * - 用户在注册时需要提交的验证码，用于邮箱验证或防止恶意注册。
   * <p>
   * - 通常有一定的有效期。
   */
  private String verificationCode;

  /**
   * 设置用户名。
   * <p>
   * 描述：
   * <p>
   * - 将同步更新 `name` 字段为用户名。
   * <p>
   * - 保证用户名和名称字段一致。
   *
   * @param username 用户名。
   */
  public void setUsername(String username) {
    this.username = username;
    this.name = username; // 同步更新 name 字段
  }
}