package com.david.hlp.SpringBootWork.system.requestentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户认证请求数据类。
 * <p>
 * 用于封装用户登录或认证请求中提交的必要信息。
 * <p>
 * 支持通过邮箱或用户名进行身份认证。
 * <p>
 * 使用了 Lombok 注解，简化了类的构造和访问器代码：
 * <p>
 * - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 * <p>
 * - @Builder 提供构建器模式，便于灵活地创建对象实例。
 * <p>
 * - @AllArgsConstructor 自动生成包含所有字段的构造函数。
 * <p>
 * - @NoArgsConstructor 自动生成无参构造函数。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  /**
   * 用户邮箱。
   * <p>
   * 描述：
   * <p>
   * - 用于标识用户的唯一登录凭证。
   * <p>
   * - 可以作为登录时的标识字段之一。
   */
  private String email;

  /**
   * 用户名。
   * <p>
   * 描述：
   * <p>
   * - 用于标识用户的唯一登录凭证。
   * <p>
   * - 允许用户通过用户名进行登录。
   */
  private String username;

  /**
   * 用户密码。
   * <p>
   * 描述：
   * <p>
   * - 用户的敏感信息，用于验证用户身份。
   * <p>
   * - 应在存储和传输时进行加密保护。
   */
  private String password;
}