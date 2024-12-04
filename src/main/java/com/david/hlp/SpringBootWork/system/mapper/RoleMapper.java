package com.david.hlp.SpringBootWork.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.entity.Role;
import com.david.hlp.SpringBootWork.system.entity.RolePermission;
import com.david.hlp.SpringBootWork.system.entity.RolePermissionInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 角色数据映射接口。
 *
 * 描述：
 * <p>
 * - 提供对角色表 (Role) 的数据库操作。
 * <p>
 * - 继承自 MyBatis-Plus 的 BaseMapper，支持通用的 CRUD 功能。
 * <p>
 * - 定义了自定义的 SQL 查询方法，用于根据角色名称或角色 ID 获取角色及其关联的权限。
 * <p>
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色名称查询角色信息。
     *
     * 描述：
     * <p>
     * - 使用自定义 SQL 查询角色表 (role) 中的记录。
     * <p>
     * - 同时加载与该角色关联的权限信息 (permissions)，通过 `PermissionMapper.selectPermissionsByRoleId` 方法实现。
     *
     * SQL 逻辑：
     * <p>
     * - 从 `role` 表中选择所有字段。
     * <p>
     * - 使用 LEFT JOIN 连接 `role_permission` 和 `permission` 表，通过角色名称过滤记录。
     * <p>
     *
     * @param roleName 角色名称。
     * @return 包含角色及其权限信息的角色对象 (Role)。
     */
    @Select("SELECT DISTINCT r.* " +
            "FROM role r " +
            "LEFT JOIN role_permission rp ON r.id = rp.role_id and rp.status = true " +
            "LEFT JOIN permission p ON rp.permission_id = p.id and p.status = true " +
            "WHERE r.role_name = #{roleName} and r.status = true ")
    @Results({
            @Result(property = "id", column = "id"), // 映射角色 ID
            @Result(property = "roleName", column = "role_name"), // 映射角色名称
            @Result(property = "permissions", javaType = List.class, column = "id",
                    many = @Many(select = "com.david.hlp.SpringBootWork.system.mapper.PermissionMapper.selectPermissionsByRoleId")) // 加载权限列表
    })
    Role selectByRoleName(@Param("roleName") String roleName);

    /**
     * 根据角色 ID 查询角色信息。
     *
     * 描述：
     * <p>
     * - 使用自定义 SQL 查询角色表 (role) 中的记录。
     * <p>
     * - 同时加载与该角色关联的权限信息 (permissions)，通过 `PermissionMapper.selectPermissionsByRoleId` 方法实现。
     *
     * SQL 逻辑：
     * <p>
     * - 从 `role` 表中选择所有字段。
     * <p>
     * - 使用 LEFT JOIN 连接 `role_permission` 和 `permission` 表，通过角色 ID 过滤记录。
     * <p>
     *
     * @param roleId 角色的唯一标识符 (ID)。
     * @return 包含角色及其权限信息的角色对象 (Role)。
     */
    @Select("SELECT DISTINCT r.* " +
            "FROM role r " +
            "LEFT JOIN role_permission rp ON r.id = rp.role_id and rp.status = true " +
            "LEFT JOIN permission p ON rp.permission_id = p.id and p.status = true " +
            "WHERE r.id = #{roleId} and r.status = true ")
    @Results({
            @Result(property = "id", column = "id"), // 映射角色 ID
            @Result(property = "roleName", column = "role_name"), // 映射角色名称
            @Result(property = "permissions", javaType = List.class, column = "id",
                    many = @Many(select = "com.david.hlp.SpringBootWork.system.mapper.PermissionMapper.selectPermissionsByRoleId")) // 加载权限列表
    })
    Role selectByRoleId(@Param("roleId") Long roleId);

    @Select("select * from role")
    List<Role> selectAllRole();

    @Update("update role set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") boolean status);

    List<Role> selectByRoleAndPermissino(int offset ,int page,int limit,String sort,String roleName, Boolean status , String permission);

    int countFilteredRoles(@Param("roleName")String roleName, @Param("status")Boolean status , @Param("permission")String permission);

    @Update("update role set " +
            "status = #{status} , " +
            "role_name = #{roleName} , " +
            "description = #{description} " +
            "where id = #{id}")
    void updataRole(@Param("roleName")String roleName , @Param("status")Boolean status , @Param("description")String description , @Param("id")Long id);

    void updateRolePermissions(@Param("rolePermission") RolePermissionInfo rolePermission);
    void insertRolePermissions(@Param("rolePermission") RolePermissionInfo rolePermission);
    void deleteRolePermissions(@Param("rolePermission") RolePermissionInfo rolePermission);
}