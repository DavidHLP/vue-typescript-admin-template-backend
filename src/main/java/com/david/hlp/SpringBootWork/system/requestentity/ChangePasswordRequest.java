package com.david.hlp.SpringBootWork.system.requestentity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户修改密码请求封装类。
 * <p>
 * 用于接收用户提交的密码修改请求数据，包括当前密码、新密码和确认新密码。
 * <p>
 * 使用了 Lombok 注解，简化了 Getter 和 Setter 方法的生成，并支持构建器模式：
 * <p>
 * - @Getter 自动生成所有字段的 Getter 方法。
 * <p>
 * - @Setter 自动生成所有字段的 Setter 方法。
 * <p>
 * - @Builder 提供构建器模式，便于灵活创建对象实例。
 */
@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    /**
     * 用户当前的密码。
     * <p>
     * 描述：
     * <p>
     * - 用于验证用户的身份，确保修改密码操作是由本人发起的。
     * <p>
     */
    private String currentPassword;

    /**
     * 用户想要设置的新密码。
     * <p>
     * 描述：
     * <p>
     * - 新密码需要符合密码强度要求（如长度和复杂度）。
     */
    private String newPassword;

    /**
     * 确认新密码。
     * <p>
     * 描述：
     * <p>
     * - 用于再次输入新密码以验证一致性，防止用户输入错误。
     */
    private String confirmationPassword;
}