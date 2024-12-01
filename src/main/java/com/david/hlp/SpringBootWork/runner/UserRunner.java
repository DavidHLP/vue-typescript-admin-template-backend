package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.Repository.UserRepository;
import com.david.hlp.SpringBootWork.system.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户初始化运行器。
 *
 * 在应用启动时运行，用于检查并初始化管理员用户。
 */
@Service // 将类标记为 Spring 服务组件
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
@DependsOn({"roleRunner"}) // 确保 RoleRunner 先于本运行器执行
public class UserRunner {

    /**
     * 用于加密和验证密码的工具。
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用于用户持久化操作的存储库。
     */
    private final UserRepository userRepository;

    /**
     * 用于角色持久化操作的存储库。
     */
    private final RoleRepository roleRepository;

    /**
     * 管理员用户的默认信息。
     */
    private final String ADMIN_NAME = "Spike";
    private final String ADMIN_PASSWORD = "#Alone117";
    private final String ADMIN_EMAIL = "Spike@163.com";

    /**
     * 初始化方法。
     *
     * 使用 @PostConstruct 注解，确保该方法在依赖注入完成后立即运行。
     * 方法功能是检查数据库中是否存在管理员用户，如果不存在则创建并保存管理员用户。
     */
    @PostConstruct
    public void init() {
        // 检查是否已经存在管理员用户
        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            // 创建管理员用户
            User adminUser = User.builder()
                    .name(ADMIN_NAME) // 设置管理员名称
                    .email(ADMIN_EMAIL) // 设置管理员邮箱
                    .password(passwordEncoder.encode(ADMIN_PASSWORD)) // 加密密码并设置
                    .role(roleRepository.findByRoleName("ADMIN")) // 设置角色为 ADMIN
                    .build();

            // 保存管理员用户到数据库
            userRepository.save(adminUser);

            // 输出日志，表示管理员用户已初始化
            System.out.println("Admin user initialized: " + ADMIN_NAME);
        } else {
            // 输出日志，表示管理员用户已存在
            System.out.println("Admin user already exists.");
        }
    }
}