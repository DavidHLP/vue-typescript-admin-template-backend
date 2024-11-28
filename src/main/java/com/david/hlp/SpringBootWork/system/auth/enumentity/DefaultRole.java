package com.david.hlp.SpringBootWork.system.auth.enumentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 定义系统中默认的角色枚举。
 */
@RequiredArgsConstructor
public enum DefaultRole {

  /**
   * 普通用户角色。
   */
  GUEST("guest"),

  /**
   * 管理员角色。
   */
  ADMIN("admin"),

  /**
   * 管理模块角色。
   */
  MANAGEMENT("management");

  /**
   * 角色名称。
   */
  @Getter
  private final String role; // 使用 final 表示枚举值不可更改
}
