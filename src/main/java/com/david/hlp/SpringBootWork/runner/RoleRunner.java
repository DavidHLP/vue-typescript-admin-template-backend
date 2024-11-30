package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.entity.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleRunner {

    private final String ROLE_GUEST = "GUEST";
    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_MANAGER = "MANAGEMENT";
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        // 检查是否已经存在这些角色，如果不存在则插入
        if (!roleRepository.existsByRoleName(ROLE_GUEST)) {
            Role userRole = new Role();
            userRole.setRoleName(ROLE_GUEST);
            roleRepository.save(userRole);
        }

        if (!roleRepository.existsByRoleName(ROLE_ADMIN)) {
            Role adminRole = new Role();
            adminRole.setRoleName(ROLE_ADMIN);
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByRoleName(ROLE_MANAGER)) {
            Role managerRole = new Role();
            managerRole.setRoleName(ROLE_MANAGER);
            roleRepository.save(managerRole);
        }
    }
}
