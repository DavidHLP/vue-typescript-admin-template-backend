package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.auth.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.auth.Repository.UserRepository;
import com.david.hlp.SpringBootWork.system.auth.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@DependsOn({"roleRunner"})
public class UserRunner {

    /**
     * 用于加密和验证密码的工具。
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用于用户持久化操作的存储库。
     */
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final String ADMIN_NAME = "Spike";
    private final String ADMIN_PASSWORD = "#Alone117";
    private final String ADMIN_EMAIL = "Spike@163.com";

    @PostConstruct
    public void init() {
        // 检查是否已经存在管理员用户
        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            // 创建管理员用户
            User adminUser = User.builder()
                    .name(ADMIN_NAME)
                    .email(ADMIN_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(roleRepository.findByRoleName("ADMIN"))
                    .build();

            // 保存管理员用户到数据库
            userRepository.save(adminUser);
            System.out.println("Admin user initialized: " + ADMIN_NAME);
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}

