package com.david.hlp.SpringBootWork.system.responsentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户认证响应数据类。
 *
 * 用于封装用户登录或刷新令牌操作后返回的认证信息（如访问令牌和刷新令牌）。
 * 使用了 Lombok 和 Jackson 注解，简化了类的构造和序列化操作。
 */
@Data // 自动生成Getter、Setter、toString、equals和hashCode方法
@Builder // 提供构建器模式用于创建对象
@AllArgsConstructor // 自动生成全参构造函数
@NoArgsConstructor  // 自动生成无参构造函数
public class AuthenticationResponse {

  /**
   * 访问令牌 (Access Token)。
   * 用于授权用户访问受保护资源，有效期较短。
   * 使用 @JsonProperty 注解指定 JSON 序列化/反序列化时的字段名称为 "access_token"。
   */
  @JsonProperty("access_token")
  private String accessToken;

  /**
   * 刷新令牌 (Refresh Token)。
   * 用于生成新的访问令牌，有效期较长。
   * 使用 @JsonProperty 注解指定 JSON 序列化/反序列化时的字段名称为 "refresh_token"。
   */
  @JsonProperty("refresh_token")
  private String refreshToken;
}
