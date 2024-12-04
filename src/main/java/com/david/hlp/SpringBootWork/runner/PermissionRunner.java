package com.david.hlp.SpringBootWork.runner;

import com.david.hlp.SpringBootWork.system.Repository.PermissionRepository;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.enumentity.DefaultRolePermission;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 权限初始化运行器。
 *
 * 在应用启动时运行，自动将默认权限插入到数据库中（如果尚未存在）。
 */
@Service // 将该类标记为 Spring 服务组件，使其在应用启动时被扫描和管理
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class PermissionRunner {

    // 注入 PermissionRepository，用于与数据库交互
    private final PermissionRepository permissionRepository;

    /**
     * 初始化方法。
     *
     * 使用 @PostConstruct 注解，确保该方法在依赖注入完成后立即运行。
     * 方法功能是检查并插入默认权限到数据库。
     */
    @PostConstruct
    public void init() {
        // 遍历枚举 DefaultRolePermission 中的所有权限值
        Arrays.stream(DefaultRolePermission.values()).forEach(permission -> {
            // 检查数据库中是否已存在该权限
            if (!permissionRepository.existsByPermission(permission.getPermission())) {
                // 如果数据库中不存在该权限，则创建新的 Permission 实体并保存
                Permission permissionEntity = new Permission(
                        null,                           // ID 为 null，由数据库自动生成
                        permission.getPermission(),     // 枚举中的权限名称
                        null                            // 其他字段留空
                        ,true
                );
                permissionRepository.save(permissionEntity); // 保存实体到数据库
            }
        });
    }
}