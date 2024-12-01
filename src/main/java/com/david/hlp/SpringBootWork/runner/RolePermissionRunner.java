package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.PermissionRepository;
import com.david.hlp.SpringBootWork.system.Repository.RolePermissionRepository;
import com.david.hlp.SpringBootWork.system.Repository.RoleRepository;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.enumentity.DefaultRole;
import com.david.hlp.SpringBootWork.system.enumentity.DefaultRolePermission;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色-权限初始化运行器。
 *
 * 在应用启动时运行，自动将角色与权限的关系插入到数据库中。
 * 依赖于 RoleRunner 和 PermissionRunner 的初始化逻辑。
 */
@Service
@RequiredArgsConstructor
@DependsOn({"roleRunner", "permissionRunner"}) // 确保依赖运行器先执行
public class RolePermissionRunner {

    // 获取所有枚举值的角色名称
    private final List<String> initRolesName = Arrays.stream(DefaultRole.values())
            .map(DefaultRole::getRole) // 获取角色名称
            .toList(); // 转换为列表

    // 获取所有枚举值的权限名称
    private final List<String> initPermissionName = Arrays.stream(DefaultRolePermission.values())
            .map(DefaultRolePermission::getPermission) // 获取权限名称
            .toList(); // 转换为列表

    private final RolePermissionRepository rolePermissionRepository; // 角色-权限关系存储库
    private final RoleRepository roleRepository; // 角色存储库
    private final PermissionRepository permissionRepository; // 权限存储库

    /**
     * 初始化方法。
     *
     * 在应用启动时运行，根据枚举中定义的角色和权限，将角色与权限的关系插入数据库。
     */
    @PostConstruct
    public void init() {
        // 获取数据库中的角色对象
        List<Role> roles = new ArrayList<>();
        for (String roleName : initRolesName) {
            roles.add(roleRepository.findByRoleName(roleName)); // 查询角色实体
        }

        // 获取数据库中的权限对象
        List<Permission> permissions = new ArrayList<>();
        for (String permissionName : initPermissionName) {
            permissions.add(permissionRepository.findByPermission(permissionName)); // 查询权限实体
        }

        // 为每个权限分配对应的角色
        for (Permission permission : permissions) {
            for (Role role : roles) {
                // 检查权限是否属于角色（通过权限名称前缀匹配）
                if (role.getRoleName().toLowerCase().equals(
                        permission.getPermission().toLowerCase().split(":")[0]
                )) {
                    // 检查是否已经存在角色-权限关系
                    boolean exists = rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId());
                    if (!exists) {
                        // 如果不存在，插入角色-权限关系
                        rolePermissionRepository.insertRolePermission(role.getId(), permission.getId());
                    }
                }
            }
        }
    }
}