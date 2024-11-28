package com.david.hlp.SpringBootWork.system.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.auth.entity.User;
import com.david.hlp.SpringBootWork.system.auth.entity.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select role_id from user where email = #{username}")
    public Long getRoleIdByUsername(@Param("username") String username);

    UserInfo getUserByUsername(@Param("username") String username);

}
