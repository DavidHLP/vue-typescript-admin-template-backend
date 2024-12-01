package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.entity.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 角色初始化运行器。
 *
 * 在应用启动时运行，用于检查并插入默认角色到数据库。
 */
@Service // 将类标记为 Spring 服务组件
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class RoleRunner {

    // 定义系统的默认角色名称
    private final String ROLE_GUEST = "GUEST";
    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_MANAGER = "MANAGEMENT";

    // 注入 RoleRepository，用于与数据库交互
    private final RoleRepository roleRepository;

    /**
     * 初始化方法。
     *
     * 使用 @PostConstruct 注解，确保该方法在依赖注入完成后立即运行。
     * 主要用于检查数据库中是否存在默认角色，如果不存在则插入。
     */
    @PostConstruct
    public void init() {
        // 检查并插入 GUEST 角色
        if (!roleRepository.existsByRoleName(ROLE_GUEST)) {
            Role userRole = new Role();
            userRole.setRoleName(ROLE_GUEST); // 设置角色名称为 GUEST
            roleRepository.save(userRole);   // 保存到数据库
        }

        // 检查并插入 ADMIN 角色
        if (!roleRepository.existsByRoleName(ROLE_ADMIN)) {
            Role adminRole = new Role();
            adminRole.setRoleName(ROLE_ADMIN); // 设置角色名称为 ADMIN
            roleRepository.save(adminRole);   // 保存到数据库
        }

        // 检查并插入 MANAGEMENT 角色
        if (!roleRepository.existsByRoleName(ROLE_MANAGER)) {
            Role managerRole = new Role();
            managerRole.setRoleName(ROLE_MANAGER); // 设置角色名称为 MANAGEMENT
            roleRepository.save(managerRole);     // 保存到数据库
        }
    }
}