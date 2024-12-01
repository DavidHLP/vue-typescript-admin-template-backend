package com.david.hlp.SpringBootWork.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限数据映射接口。
 *
 * 描述：
 * <p>
 * - 提供对权限表 (Permission) 的数据库操作。
 * <p>
 * - 继承自 MyBatis-Plus 的 BaseMapper，支持通用的 CRUD 操作。
 * <p>
 * - 定义了自定义的 SQL 查询方法，根据角色 ID 获取对应的权限列表。
 * <p>
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据角色 ID 查询权限列表。
     *
     * 描述：
     * <p>
     * - 使用自定义 SQL 查询权限表 (permission) 中的记录。
     * <p>
     * - 通过连接角色-权限关联表 (role_permission)，获取指定角色拥有的权限。
     *
     * SQL 逻辑：
     * <p>
     * - 从 `permission` 表中选择所有字段。
     * <p>
     * - 使用 LEFT JOIN 连接 `role_permission` 表，通过角色 ID (`role_id`) 过滤关联记录。
     *
     * @param roleId 角色的唯一标识符 (ID)。
     * @return 包含权限信息的列表 (List<Permission>)。
     */
    @Select("SELECT p.* FROM permission p " +
            "LEFT JOIN role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}