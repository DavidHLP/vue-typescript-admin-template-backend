package com.david.hlp.SpringBootWork.system.auth.requestentity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户修改密码请求的封装类。
 * 包含用户当前密码、新密码以及确认新密码的信息。
 */
@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    /**
     * 用户当前的密码。
     */
    private String currentPassword;

    /**
     * 用户想要设置的新密码。
     */
    private String newPassword;

    /**
     * 确认新密码，用于验证新密码的输入一致性。
     */
    private String confirmationPassword;
}
