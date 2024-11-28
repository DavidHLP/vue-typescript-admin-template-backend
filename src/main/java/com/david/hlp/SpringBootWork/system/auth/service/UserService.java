package com.david.hlp.SpringBootWork.system.auth.service;

import com.david.hlp.SpringBootWork.system.auth.entity.Permission;
import com.david.hlp.SpringBootWork.system.auth.entity.Role;
import com.david.hlp.SpringBootWork.system.auth.entity.UserInfo;
import com.david.hlp.SpringBootWork.system.auth.mapper.RoleMapper;
import com.david.hlp.SpringBootWork.system.auth.mapper.UserMapper;
import com.david.hlp.SpringBootWork.system.auth.requestentity.ChangePasswordRequest;
import com.david.hlp.SpringBootWork.system.auth.entity.User;
import com.david.hlp.SpringBootWork.system.auth.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类，处理与用户相关的业务逻辑。
 * 提供更改密码的功能。
 */
@Service
@RequiredArgsConstructor // 自动生成构造函数，注入必要的依赖
public class UserService {

    /**
     * 用于加密和验证密码的工具。
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户数据访问层，用于与数据库交互。
     */
    private final UserRepository repository;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    /**
     * 更改用户密码的方法。
     *
     * @param request 包含用户当前密码、新密码和确认密码的请求数据。
     * @param connectedUser 当前已认证的用户信息，由 Spring Security 提供的 Principal 对象表示。
     */
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        // 从 Principal 中获取当前用户对象
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // 验证当前密码是否正确
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("当前密码错误"); // 抛出异常，提示密码错误
        }

        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("新密码和确认密码不一致"); // 抛出异常，提示密码不一致
        }

        // 设置新的加密密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 保存更新后的用户对象到数据库
        repository.save(user);
    }

    public List<SimpleGrantedAuthority> getAuthorities(String username) {
        Long roleId = userMapper.getRoleIdByUsername(username);

        Role role = roleMapper.selectByRoleId(roleId);

        List<Permission> permissions = role.getPermissions();
        String roleName = role.getRoleName();

        // 判空处理
        if (permissions == null || permissions.isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
        }

        // 转换权限列表
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        // 添加角色标识符
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        return authorities;
    }


    public UserInfo getUser(String username) {
        return userMapper.getUserByUsername(username);
    }
}