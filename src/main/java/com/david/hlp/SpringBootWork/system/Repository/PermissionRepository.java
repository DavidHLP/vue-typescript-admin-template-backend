package com.david.hlp.SpringBootWork.system.Repository;

import com.david.hlp.SpringBootWork.system.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    // 根据权限标识符查询是否存在该权限
    boolean existsByPermission(String permission);
    Permission findByPermission(String permission);
}
