package com.david.hlp.SpringBootWork.system.responsentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户认证响应数据类。
 * <p>
 * 用于封装用户认证操作（如登录、注册或刷新令牌）后的响应数据。
 * <p>
 * 提供了访问令牌和刷新令牌，用于用户的认证和会话管理。
 * <p>
 * 使用了 Lombok 注解简化类的构造，同时结合 Jackson 注解实现 JSON 数据的序列化和反序列化。
 */
@Data // 自动生成 Getter、Setter、toString、equals 和 hashCode 方法
@Builder // 提供构建器模式用于创建对象实例
@AllArgsConstructor // 自动生成包含所有字段的构造函数
@NoArgsConstructor  // 自动生成无参构造函数
public class AuthenticationResponse {

  /**
   * 访问令牌 (Access Token)。
   * <p>
   * 描述：
   * <p>
   * - 短期有效的令牌，用于授权用户访问受保护资源。
   * <p>
   * - 通常在客户端请求时附带此令牌，用于验证用户身份。
   * <p>
   * Jackson 注解：
   * <p>
   * - 使用 @JsonProperty 指定序列化和反序列化时的字段名称为 "access_token"。
   */
  @JsonProperty("access_token")
  private String accessToken;

  /**
   * 刷新令牌 (Refresh Token)。
   * <p>
   * 描述：
   * <p>
   * - 长期有效的令牌，用于获取新的访问令牌。
   * <p>
   * - 在访问令牌过期时，客户端可以使用刷新令牌获取一个新的访问令牌，而无需重新登录。
   * <p>
   * Jackson 注解：
   * <p>
   * - 使用 @JsonProperty 指定序列化和反序列化时的字段名称为 "refresh_token"。
   */
  @JsonProperty("refresh_token")
  private String refreshToken;
}