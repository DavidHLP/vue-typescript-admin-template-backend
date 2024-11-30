package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.service.imp.MailServiceImp;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Title: SpringMailController
 * @Author David
 * @Package com.work.spring.springbootwork.controller
 * @Date 2024/9/11 下午1:05
 */
@RestController
public class SpringMailController {

    @Resource
    private MailServiceImp mailService;

    // 重置密码请求
    @GetMapping("/resetpassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("code") String code,
                                @RequestParam("password") String password) {
        return mailService.resetPassword(email, code, password);
    }
}

