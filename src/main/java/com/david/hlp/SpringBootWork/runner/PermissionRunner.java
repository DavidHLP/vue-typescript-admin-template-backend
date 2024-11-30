package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.PermissionRepository;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.enumentity.DefaultRolePermission;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PermissionRunner {

    private final PermissionRepository permissionRepository;

    @PostConstruct
    public void init() {
        // 遍历枚举类 Permission，插入数据库
        Arrays.stream(DefaultRolePermission.values()).forEach(permission -> {
            // 查询数据库是否已存在该权限
            if (!permissionRepository.existsByPermission(permission.getPermission())) {
                // 如果数据库中不存在该权限，则插入
                Permission permissionEntity = new Permission(null, permission.getPermission(), null);
                permissionRepository.save(permissionEntity);
            }
        });
    }
}