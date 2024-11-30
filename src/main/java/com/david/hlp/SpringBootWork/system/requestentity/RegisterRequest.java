package com.david.hlp.SpringBootWork.system.requestentity;

import com.david.hlp.SpringBootWork.system.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求数据类。
 *
 * 用于封装用户在注册操作中提交的必要信息。
 * 使用 Lombok 注解简化构造和访问器的生成。
 */
@Data // 自动生成Getter、Setter、toString、equals和hashCode方法
@Builder // 提供构建器模式用于创建对象
@AllArgsConstructor // 自动生成全参构造函数
@NoArgsConstructor  // 自动生成无参构造函数
public class RegisterRequest {

  /**
   * 用户的名字。
   */
  private String name;

  /**
   * 用户的邮箱。
   * 用于标识用户的唯一凭证。
   */
  private String email;

  /**
   * 用户的密码。
   * 将在服务端进行加密存储。
   */
  private String password;

  /**
   * 用户的角色。
   * 指定用户的权限级别（如管理员、普通用户等）。
   */
  private Role role;

  private String username;

  private String verificationCode;

  public void setUsername(String username) {
    this.username = username;
    this.name = username;
  }
}
