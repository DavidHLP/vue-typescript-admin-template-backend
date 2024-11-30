package com.david.hlp.SpringBootWork.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT DISTINCT r.* " +
            "FROM role r " +
            "LEFT JOIN role_permission rp ON r.id = rp.role_id " +
            "LEFT JOIN permission p ON rp.permission_id = p.id " +
            "WHERE r.role_name = #{roleName}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "permissions", javaType = List.class, column = "id",
                    many = @Many(select = "com.david.hlp.SpringBootWork.system.mapper.PermissionMapper.selectPermissionsByRoleId"))
    })
    Role selectByRoleName(@Param("roleName") String roleName);


    @Select("SELECT DISTINCT r.* " +
            "FROM role r " +
            "LEFT JOIN role_permission rp ON r.id = rp.role_id " +
            "LEFT JOIN permission p ON rp.permission_id = p.id " +
            "WHERE r.id = #{roleId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "permissions", javaType = List.class, column = "id",
                    many = @Many(select = "com.david.hlp.SpringBootWork.system.mapper.PermissionMapper.selectPermissionsByRoleId"))
    })
    Role selectByRoleId(@Param("roleId") Long roleId);
}

