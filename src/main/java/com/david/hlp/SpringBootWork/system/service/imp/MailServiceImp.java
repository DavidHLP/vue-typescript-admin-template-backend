package com.david.hlp.SpringBootWork.system.service.imp;

import com.david.hlp.SpringBootWork.system.util.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Title: MailServiceImp
 * @Author: David
 * @Description: 邮件服务实现类，负责发送验证码邮件及重置密码功能。
 * 包含生成验证码、邮件发送、验证码校验等逻辑。
 * @Date: 2024/12/1 下午4:40
 */

@Service
public class MailServiceImp {

    @Resource
    private JavaMailSender mailSender; // 邮件发送器

    @Value("${spring.mail.username}")
    private String from; // 邮件发送者邮箱地址，从配置文件读取

    @Value("${spring.mail.outTime}")
    private Integer outTime; // 验证码过期时间，从配置文件读取

    @Resource
    private RedisCache redisCache; // Redis 缓存工具类，用于缓存验证码和发送状态

    /**
     * 生成六位数字验证码。
     * <p>
     * 随机生成一个六位数字的验证码，用于邮件验证。
     *
     * @return 返回六位数字验证码字符串。
     */
    public String generateSixDigitCode() {
        String digits = "0123456789"; // 数字字符集合
        StringBuilder code = new StringBuilder(); // 用于存储生成的验证码
        Random random = new Random(); // 随机数生成器
        for (int i = 0; i < 6; i++) {
            code.append(digits.charAt(random.nextInt(digits.length())));
        }
        return code.toString();
    }

    /**
     * 发送验证码邮件。
     * <p>
     * 检查 Redis 中是否已有发送记录，避免重复发送。
     * <p>
     * 生成验证码并发送邮件，同时缓存验证码和发送状态。
     *
     * @param email 接收验证码的目标邮箱地址。
     * @return 返回邮件发送状态信息。
     */
    public String sendMail(String email) {
        // 检查 Redis 中是否已有发送记录，避免重复发送
        if (redisCache.hasKey("send_" + email)) {
            throw new RuntimeException("邮件已经发送");
        }

        String work = "注册账号";

        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【验证码】"); // 邮件主题
        String code = generateSixDigitCode(); // 生成验证码

        // 设置邮件内容
        message.setText("您好，\n\n您正在尝试"+work+"，您的验证码是："
                + code
                + "。\n验证码有效期为"
                + outTime
                + "分钟，请及时使用。\n\n如果非本人操作，请忽略此邮件，感谢您的理解！");
        message.setTo(email); // 设置接收者邮箱地址
        message.setFrom(from); // 设置发送者邮箱地址

        // 发送邮件
        mailSender.send(message);

        // 缓存发送状态，设置 1 分钟有效期，避免短时间重复发送
        redisCache.setCacheObject("send_" + email, null, 1, TimeUnit.MINUTES);

        // 删除旧的验证码（如果存在）
        redisCache.deleteObject(email);

        // 缓存新的验证码，设置过期时间为配置中的 outTime 分钟
        redisCache.setCacheObject(email, code, outTime, TimeUnit.MINUTES);

        return "邮件发送成功";
    }

    /**
     * 重置密码逻辑。
     * <p>
     * 验证用户输入的验证码是否正确，并根据验证结果决定是否重置密码。
     *
     * @param email 用户的邮箱地址。
     * @param code 用户输入的验证码。
     * @param password 新密码。
     * @return 返回密码重置状态信息。
     */
    public String resetPassword(String email, String code, String password) {
        // 检查 Redis 中是否存在验证码缓存
        if (!redisCache.hasKey(email)) {
            return "验证码已过期，请重新获取";
        }

        // 从 Redis 中获取缓存的验证码
        String cachedCode = redisCache.getCacheObject(email);

        // 验证用户输入的验证码是否匹配
        if (cachedCode != null && cachedCode.equals(code)) {
            // 验证码匹配，执行密码重置逻辑
            redisCache.deleteObject(email); // 删除验证码缓存
            return "密码重置成功，新密码为：" + password; // 返回成功信息
        }

        return "验证码错误或已过期，请重新尝试"; // 验证失败时返回错误信息
    }
}