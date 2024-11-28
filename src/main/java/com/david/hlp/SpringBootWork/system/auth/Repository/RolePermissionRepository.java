package com.david.hlp.SpringBootWork.system.auth.Repository;

import com.david.hlp.SpringBootWork.system.auth.entity.Permission;
import com.david.hlp.SpringBootWork.system.auth.entity.Role;
import com.david.hlp.SpringBootWork.system.auth.entity.RolePermission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission,Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO role_permission (role_id, permission_id) VALUES (:roleId, :permissionId)", nativeQuery = true)
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 检查指定角色和权限的关联是否已存在
     * @param role 角色
     * @param permission 权限
     * @return 如果存在返回 true，否则返回 false
     */
    boolean existsByRoleAndPermission(Role role, Permission permission);
}
