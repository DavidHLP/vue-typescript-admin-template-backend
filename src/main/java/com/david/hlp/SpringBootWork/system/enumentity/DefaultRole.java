package com.david.hlp.SpringBootWork.system.enumentity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统默认角色枚举。
 *
 * 描述：
 * <p>
 * - 定义系统中的默认角色类型。
 * <p>
 * - 每个角色类型对应一个固定的角色名称，用于权限控制和角色分配。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Getter 自动生成 `role` 字段的 Getter 方法。
 *   - @RequiredArgsConstructor 自动生成带 `final` 字段的构造函数。
 * <p>
 */
@RequiredArgsConstructor
public enum DefaultRole {

  /**
   * 普通用户角色。
   *
   * 描述：
   * - 表示系统中的普通用户，通常具有最基本的访问权限。
   */
  GUEST("guest"),

  /**
   * 管理员角色。
   *
   * 描述：
   * - 表示系统管理员，通常具有最高权限，可以管理系统中的所有资源。
   */
  ADMIN("admin"),

  /**
   * 管理模块角色。
   *
   * 描述：
   * - 表示具有管理模块权限的用户，用于特定模块的管理功能。
   */
  MANAGEMENT("management");

  /**
   * 角色名称。
   *
   * 描述：
   * - 每个枚举值对应的角色名称，用于标识该角色的唯一字符串。
   * - 通常存储于数据库中，与用户角色进行匹配。
   */
  @Getter
  private final String role; // 使用 final 表示枚举值不可更改
}