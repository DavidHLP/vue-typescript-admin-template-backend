package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.auth.Repository.PermissionRepository;
import com.david.hlp.SpringBootWork.system.auth.Repository.RolePermissionRepository;
import com.david.hlp.SpringBootWork.system.auth.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.auth.entity.Permission;
import com.david.hlp.SpringBootWork.system.auth.enumentity.DefaultRole;
import com.david.hlp.SpringBootWork.system.auth.enumentity.DefaultRolePermission;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.david.hlp.SpringBootWork.system.auth.entity.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@DependsOn({"roleRunner", "permissionRunner"})
public class RolePermissionRunner {

    private final List<String> initRolesName = Arrays.stream(DefaultRole.values())  // 获取所有枚举值
            .map(DefaultRole::getRole)  // 获取每个枚举值的权限标识符
            .toList();  // 转换为列表

    private final List<String> initPermissionName = Arrays.stream(DefaultRolePermission.values())  // 获取所有枚举值
            .map(DefaultRolePermission::getPermission)  // 获取每个枚举值的权限标识符
            .toList();  // 转换为列表

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @PostConstruct
    public void init() {
        List<Role> roles = new ArrayList<>();
        for (String roleName : initRolesName) {
            roles.add(roleRepository.findByRoleName(roleName));
        }
        List<Permission> permissions = new ArrayList<>();
        for (String permissionName : initPermissionName) {
            permissions.add(permissionRepository.findByPermission(permissionName));
        }

        for (Permission permission : permissions) {
            for (Role role : roles) {
                if (role.getRoleName().toLowerCase().
                        equals(
                                permission.getPermission().toLowerCase().
                                        split(":")[0]
                        )
                ) {
                    rolePermissionRepository.insertRolePermission(role.getId(), permission.getId());
                }
            }
        }
    }
}