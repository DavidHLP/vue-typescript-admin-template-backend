package com.david.hlp.SpringBootWork.system.Repository;

import com.david.hlp.SpringBootWork.system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色数据访问层接口。
 *
 * 描述：
 * <p>
 * - 提供对角色表 (Role) 的基本 CRUD 操作。
 * <p>
 * - 定义了基于角色名称的自定义查询方法，用于满足业务需求。
 * <p>
 * - 继承自 JpaRepository，支持分页、排序和其他数据操作功能。
 * <p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 检查是否存在指定的角色名称。
     *
     * 描述：
     * <p>
     * - 根据角色名称 (roleName) 检查数据库中是否存在对应的角色记录。
     * <p>
     * - 用于验证角色的唯一性或角色是否已分配。
     *
     * @param roleName 角色名称。
     * @return 如果角色存在，返回 true；否则返回 false。
     */
    boolean existsByRoleName(String roleName);

    /**
     * 根据角色名称查询角色信息。
     *
     * 描述：
     * <p>
     * - 根据角色名称 (roleName) 查询数据库中的角色对象。
     * <p>
     * - 通常用于角色权限分配或身份验证相关逻辑。
     *
     * @param roleName 角色名称。
     * @return 对应的角色对象 (Role)。如果角色不存在，返回 null。
     */
    Role findByRoleName(String roleName);
}
