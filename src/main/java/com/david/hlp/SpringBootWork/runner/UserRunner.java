package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.Repository.UserRepository;
import com.david.hlp.SpringBootWork.system.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户初始化运行器。
 *
 * 在应用启动时运行，用于检查并初始化系统用户。
 */
@Service
@RequiredArgsConstructor
@DependsOn({"roleRunner"})
public class UserRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        // 定义默认用户列表
        List<User> defaultUsers = new ArrayList<>();
        defaultUsers.add( User.builder()
                .name("Admin User")
                .email("admin@system.com")
                .password(passwordEncoder.encode("admin123"))
                .role(roleRepository.findByRoleName("Admin"))
                .introduction("测试账号")
                .status(true)
                .build()
        );
        defaultUsers.addAll(createUsers("Admin", "ADMIN", 5, "我是管理员，负责系统的管理与权限配置。"));
        defaultUsers.addAll(createUsers("Manager", "MANAGEMENT", 8, "我是管理者，负责协调事务与资源分配。"));
        defaultUsers.addAll(createUsers("Guest", "GUEST", 7, "我是访客，访问公开内容，了解基本功能。"));

        // 遍历用户列表并检查是否已经存在
        defaultUsers.forEach(user -> {
            if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
                userRepository.save(user);
            }
        });
    }

    /**
     * 创建指定数量的用户
     *
     * @param rolePrefix 角色前缀（如 Admin、Manager、Guest）
     * @param roleName   用户角色
     * @param count      用户数量
     * @param intro      用户简介
     * @return 创建的用户列表
     */
    private List<User> createUsers(String rolePrefix, String roleName, int count, String intro) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            users.add(
                    User.builder()
                            .name(rolePrefix + " User " + i)
                            .email(rolePrefix.toLowerCase() + i + "@system.com")
                            .password(passwordEncoder.encode("admin123"))
                            .role(roleRepository.findByRoleName(roleName))
                            .introduction(intro + " 用户编号：" + i)
                            .status(i%2 == 0)
                            .build()
            );
        }
        return users;
    }
}