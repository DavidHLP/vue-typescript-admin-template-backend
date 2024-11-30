package com.david.hlp.SpringBootWork.system.token;

/**
 * 表示应用程序中使用的令牌类型的枚举类。
 * 当前仅支持 BEARER 类型，通常用于 Web 应用程序中的身份认证。
 */
public enum TokenType {
  /**
   * BEARER 类型令牌，通常用于 HTTP Authorization 头中。
   * 示例：
   * Authorization: Bearer <token>
   */
  BEARER
}
