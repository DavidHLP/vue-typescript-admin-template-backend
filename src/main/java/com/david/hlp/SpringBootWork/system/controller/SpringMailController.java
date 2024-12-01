package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.service.imp.MailServiceImp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件控制器类。
 *
 * 描述：
 * <p>
 * - 提供与邮件相关的 RESTful API 接口。
 * <p>
 * - 当前实现的功能包括通过邮件验证码重置密码。
 * <p>
 * - 使用 Spring MVC 注解实现控制器功能。
 */
@RestController
public class SpringMailController {

    /**
     * 邮件服务实现类。
     *
     * 描述：
     * - 注入 `MailServiceImp`，用于处理与邮件发送和验证相关的具体业务逻辑。
     * - 使用 `@Resource` 注解进行依赖注入。
     */
    @Resource
    private MailServiceImp mailService;

    /**
     * 重置密码接口。
     *
     * 描述：
     * <p>
     * - 提供通过邮件验证码验证的密码重置功能。
     * <p>
     * - 用户需要提供邮箱地址、验证码和新密码。
     * <p>
     * - 调用 `MailServiceImp` 的 `resetPassword` 方法处理业务逻辑。
     *
     * 请求方式：
     * <p>
     * - GET 请求路径为 `/resetpassword`。
     *
     * 请求参数：
     * <p>
     * - `email` (String)：用户的邮箱地址，用于标识账号。
     * <p>
     * - `code` (String)：验证码，用于验证邮箱的有效性。
     * <p>
     * - `password` (String)：新密码，用于更新用户账号密码。
     *
     * 返回值：
     * <p>
     * - 返回操作结果的消息字符串，例如 "密码重置成功" 或错误提示信息。
     *
     * @param email 用户的邮箱地址。
     * @param code 邮件验证码。
     * @param password 用户的新密码。
     * @return 返回操作结果消息。
     */
    @GetMapping("/resetpassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("code") String code,
                                @RequestParam("password") String password) {
        // 调用邮件服务的重置密码方法，并返回操作结果
        return mailService.resetPassword(email, code, password);
    }
}