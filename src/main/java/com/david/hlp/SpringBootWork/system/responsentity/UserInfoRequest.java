package com.david.hlp.SpringBootWork.system.responsentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRequest {

    private Long id;

    /**
     * 用户的姓名。
     *
     * 描述：
     * - 表示用户的真实姓名或显示名称。
     */
    private String name;

    /**
     * 用户的头像 URL。
     *
     * 描述：
     * - 指向用户头像的存储位置，通常是一个 URL。
     */
    private String avatar;

    /**
     * 用户的个人简介。
     *
     * 描述：
     * - 用户的个性化描述或简要信息。
     */
    private String introduction;

    /**
     * 用户的电子邮件地址。
     *
     * 描述：
     * - 用于标识用户的唯一标识符。
     * - 在系统中通常作为用户名或联系方式。
     */
    private String email;

    private Boolean status;

    private Long roleId;

}
