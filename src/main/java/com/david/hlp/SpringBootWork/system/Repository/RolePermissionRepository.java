package com.david.hlp.SpringBootWork.system.Repository;

import com.david.hlp.SpringBootWork.system.entity.Permission;
import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.entity.RolePermission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 角色-权限关联数据访问层接口。
 *
 * 描述：
 * <p>
 * - 提供对角色和权限关联表 (role_permission) 的数据库访问操作。
 * <p>
 * - 继承自 JpaRepository，支持基本的 CRUD 操作。
 * <p>
 * - 定义了插入角色权限关系和检查关联存在性的方法。
 * <p>
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * 插入角色和权限的关联关系。
     * <p>
     * 描述：
     * <p>
     * - 使用原生 SQL 插入角色和权限的关联记录到 role_permission 表中。
     * <p>
     * - 方法需要事务管理并支持修改操作。
     *
     * @param roleId 角色的 ID。
     * @param permissionId 权限的 ID。
     * @return 插入操作影响的行数。
     */
    @Modifying // 标记为修改操作
    @Transactional // 启用事务管理
    @Query(value = "INSERT INTO role_permission (role_id, permission_id) VALUES (:roleId, :permissionId)", nativeQuery = true)
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 检查指定角色和权限的关联关系是否已存在。
     * <p>
     * 描述：
     * <p>
     * - 根据角色和权限对象检查数据库中是否存在对应的关联记录。
     * <p>
     * - 通常用于避免重复插入或验证关联有效性。
     *
     * @param role 角色对象。
     * @param permission 权限对象。
     * @return 如果存在关联关系，返回 true；否则返回 false。
     */
    boolean existsByRoleAndPermission(Role role, Permission permission);

    /**
     * 检查指定角色和权限的关系是否已存在。
     *
     * @param roleId       角色的唯一标识。
     * @param permissionId 权限的唯一标识。
     * @return 如果角色-权限关系已存在，返回 true；否则返回 false。
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
}