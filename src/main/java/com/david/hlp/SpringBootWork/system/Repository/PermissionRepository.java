package com.david.hlp.SpringBootWork.system.Repository;

import com.david.hlp.SpringBootWork.system.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 权限数据访问层接口。
 * <p>
 * 描述：
 * <p>
 * - 提供对权限表 (Permission) 的数据库访问操作。
 * <p>
 * - 继承自 JpaRepository，支持常用的 CRUD 操作和分页功能。
 * <p>
 * - 额外定义了根据权限标识符查询的自定义方法。
 * <p>
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 检查是否存在指定的权限标识符。
     * <p>
     * 描述：
     * <p>
     * - 根据权限名称 (permission) 判断数据库中是否存在对应的权限记录。
     *
     * @param permission 权限标识符。
     * @return 如果权限存在，返回 true；否则返回 false。
     */
    boolean existsByPermission(String permission);

    /**
     * 根据权限标识符查询权限信息。
     * <p>
     * 描述：
     * <p>
     * - 根据权限名称 (permission) 查询对应的权限对象。
     *
     * @param permission 权限标识符。
     * @return 对应的权限对象 (Permission)。
     */
    Permission findByPermission(String permission);
}