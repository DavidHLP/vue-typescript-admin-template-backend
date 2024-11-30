package com.david.hlp.SpringBootWork.system.service.imp;

import com.david.hlp.SpringBootWork.system.util.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Title: MailServiceImp
 * @Author David
 * @Package com.work.spring.springbootwork.service
 * @Date 2024/9/11 下午4:40
 */

@Service
public class MailServiceImp {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.outTime}")
    private Integer outTime;

    @Resource
    private RedisCache redisCache;

    // 生成六位数字验证码
    public String generateSixDigitCode() {
        String digits = "0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(digits.charAt(random.nextInt(digits.length())));
        }
        return code.toString();
    }

    // 发送验证码邮件
    public String sendMail(String email) {

        if (redisCache.hasKey("send_"+email)){
            throw new RuntimeException("邮件已经发送");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【验证码】");
        String code = generateSixDigitCode();

        // 邮件内容
        message.setText("您好，\n\n您正在尝试重置密码，您的验证码是："
                + code
                + "。\n验证码有效期为"
                +outTime
                +"分钟，请及时使用。\n\n如果非本人操作，请忽略此邮件，感谢您的理解！");

        message.setTo(email);
        message.setFrom(from);

        // 发送邮件
        mailSender.send(message);

        redisCache.setCacheObject(
                "send_"+email,
                null,
                1,
                TimeUnit.MINUTES
        );

        redisCache.deleteObject(email);
        // 将验证码存入Redis，设置outTime分钟过期
        redisCache.setCacheObject(email, code, outTime, TimeUnit.MINUTES);

        return "邮件发送成功";
    }

    // 重置密码逻辑
    public String resetPassword(String email, String code, String password) {
        // 检查验证码是否过期
        if (!redisCache.hasKey(email)) {
            return "验证码已过期，请重新获取";
        }

        // 获取Redis中的验证码
        String cachedCode = redisCache.getCacheObject(email);

        // 验证码匹配
        if (cachedCode != null && cachedCode.equals(code)) {
            // 重置密码逻辑（此处可以加入实际的重置密码功能）
            redisCache.deleteObject(email); // 删除缓存中的验证码
            return "密码重置成功，新密码为：" + password;
        }

        return "验证码错误或已过期，请重新尝试";
    }
}

