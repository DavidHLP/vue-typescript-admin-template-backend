package com.david.hlp.SpringBootWork.system.requestentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户认证请求数据类。
 *
 * 用于封装用户在登录请求中提交的认证信息（如邮箱和密码）。
 * 使用了 Lombok 注解，简化了类的构造和访问器代码。
 */
@Data // 自动生成Getter、Setter、toString、equals和hashCode方法
@Builder // 提供构建器模式用于创建对象
@AllArgsConstructor // 自动生成全参构造函数
@NoArgsConstructor  // 自动生成无参构造函数
public class AuthenticationRequest {

  /**
   * 用户邮箱。
   * 用于标识用户的唯一登录凭证。
   */
  private String email;

  /**
   * 用户名称。
   * 用于标识用户的唯一登录凭证。
   */
  private String username;

  /**
   * 用户密码。
   * 用于验证用户身份的敏感信息。
   */
  private String password;
}
