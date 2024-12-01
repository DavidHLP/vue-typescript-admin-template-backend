package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果实体类。
 *
 * 描述：
 * <p>
 * - 用于封装用户登录操作的结果数据。
 * <p>
 * - 包含认证生成的访问令牌 (Access Token)，可选字段。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 *   - @Builder 提供构建器模式，用于灵活创建对象实例。
 *   - @AllArgsConstructor 自动生成包含所有字段的构造函数。
 *   - @NoArgsConstructor 自动生成无参构造函数。
 * <p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogInResult {

    /**
     * 访问令牌 (Access Token)。
     *
     * 描述：
     * - 用户登录成功后生成的 JWT 令牌。
     * - 用于后续的请求认证和授权操作。
     * - 此字段为可选字段，当登录失败或无需令牌时可能为空。
     */
    private String accessToken;
}