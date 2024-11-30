package com.david.hlp.SpringBootWork.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.entity.User;
import com.david.hlp.SpringBootWork.system.entity.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select role_id from user where email = #{username}")
    public Long getRoleIdByUsername(@Param("username") String username);

    UserInfo getUserByUsername(@Param("username") String username);

    @Select("select * from user where name like concat('%', #{name}, '%')")
    List<UserInfo> getUsersByName(@Param("name") String name);

}
