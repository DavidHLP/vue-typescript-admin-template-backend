package com.david.hlp.SpringBootWork.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.entity.User;
import com.david.hlp.SpringBootWork.system.entity.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户数据映射接口。
 *
 * 描述：
 * <p>
 * - 提供对用户表 (User) 的数据库操作。
 * <p>
 * - 继承自 MyBatis-Plus 的 BaseMapper，支持通用的 CRUD 功能。
 * <p>
 * - 定义了自定义的 SQL 查询方法，用于满足复杂的业务需求。
 * <p>
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名 (邮箱) 获取角色 ID。
     *
     * 描述：
     * <p>
     * - 使用自定义 SQL 查询 `user` 表，根据用户名 (email) 获取对应的角色 ID。
     * <p>
     * - 通常用于权限验证或角色关联操作。
     *
     * @param username 用户名 (通常是邮箱)。
     * @return 用户的角色 ID。
     */
    @Select("select role_id from user where email = #{username}")
    Long getRoleIdByUsername(@Param("username") String username);

    /**
     * 根据用户名获取用户详细信息。
     *
     * 描述：
     * <p>
     * - 从数据库查询用户的详细信息。
     * <p>
     * - 此方法可以通过 MyBatis 的 XML 配置文件或注解实现复杂查询逻辑。
     *
     * @param username 用户名 (通常是邮箱)。
     * @return 包含用户详细信息的 UserInfo 对象。
     */
    UserInfo getUserByUsername(@Param("username") String username);

    /**
     * 根据姓名模糊查询用户信息列表。
     *
     * 描述：
     * <p>
     * - 使用自定义 SQL 查询 `user` 表，根据姓名关键字模糊匹配用户记录。
     * <p>
     * - 返回所有符合条件的用户信息，用于展示或业务处理。
     *
     * SQL 逻辑：
     * <p>
     * - 查询 `user` 表中 `name` 包含指定关键字的记录。
     * <p>
     * - 使用 SQL 的 `LIKE` 运算符和 `CONCAT` 函数实现模糊匹配。
     *
     * @param name 用户姓名关键字。
     * @return 包含符合条件用户信息的列表 (List<UserInfo>)。
     */
    @Select("select * from user where name like concat('%', #{name}, '%')")
    List<UserInfo> getUsersByName(@Param("name") String name);
}