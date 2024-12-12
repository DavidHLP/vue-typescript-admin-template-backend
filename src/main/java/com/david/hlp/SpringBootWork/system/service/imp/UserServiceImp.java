package com.david.hlp.SpringBootWork.system.service.imp;

import com.david.hlp.SpringBootWork.blog.entity.ArticleWrapper;
import com.david.hlp.SpringBootWork.blog.util.DraggableTableResult;
import com.david.hlp.SpringBootWork.system.entity.*;
import com.david.hlp.SpringBootWork.system.mapper.RoleMapper;
import com.david.hlp.SpringBootWork.system.mapper.UserMapper;
import com.david.hlp.SpringBootWork.system.requestentity.ChangePasswordRequest;
import com.david.hlp.SpringBootWork.system.Repository.UserRepository;
import com.david.hlp.SpringBootWork.system.responsentity.ResponsePage;
import com.david.hlp.SpringBootWork.system.responsentity.UserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类。
 * <p>
 * 负责处理与用户相关的业务逻辑，包括更改密码、获取用户权限和用户信息等。
 */
@Service
@RequiredArgsConstructor // 自动生成构造函数，注入必要的依赖
public class UserServiceImp {

    /**
     * 用于加密和验证密码的工具。
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户数据访问层，用于与数据库交互。
     */
    private final UserRepository repository;

    /**
     * 用户数据映射层，用于执行用户相关的数据库查询。
     */
    private final UserMapper userMapper;

    /**
     * 角色数据映射层，用于执行角色相关的数据库查询。
     */
    private final RoleMapper roleMapper;

    /**
     * 更改用户密码的方法。
     * <p>
     * 验证当前密码的正确性，并检查新密码和确认密码的一致性。
     * <p>
     * 然后将加密的新密码更新到数据库。
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

    /**
     * 获取用户的权限信息。
     * <p>
     * 根据用户名查询角色和权限列表，并构建 Spring Security 的权限集合。
     *
     * @param username 用户名。
     * @return 包含角色和权限的列表，格式为 Spring Security 的 SimpleGrantedAuthority。
     */
    public List<SimpleGrantedAuthority> getAuthorities(String username) {
        // 根据用户名获取角色 ID
        Long roleId = userMapper.getRoleIdByUsername(username);

        // 根据角色 ID 获取角色信息
        Role role = roleMapper.selectByRoleId(roleId);

        // 获取角色的权限列表
        List<Permission> permissions = role.getPermissions();
        String roleName = role.getRoleName();

        // 判空处理：如果权限列表为空，则仅添加角色标识
        if (permissions == null || permissions.isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
        }

        // 转换权限列表为 SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        // 添加角色标识符
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        return authorities;
    }

    /**
     * 获取用户的详细信息。
     * <p>
     * 根据用户名查询数据库，返回用户信息。
     *
     * @param username 用户名。
     * @return 返回包含用户详细信息的 UserInfo 对象。
     */
    public UserInfo getUser(String username) {
        return userMapper.getUserByUsername(username);
    }

    /**
     * 获取符合条件的用户列表。
     * <p>
     * 根据用户名模糊查询，返回分页结果。
     *
     * @param name 用户名关键字，用于模糊查询。
     * @return 包含用户信息列表的分页结果 ResponsePage。
     */
    public ResponsePage<UserInfo> getUsers(String name) {
        // 查询符合条件的用户列表，并构建分页结果
        return ResponsePage.<UserInfo>builder().items(userMapper.getUsersByName(name)).build();
    }

    public void updateAvatarByUsername(String username, String avatar) {
        userMapper.updateAvatar(username, avatar);
    }

    public void disableUser(Long id) {
        userMapper.updateStatus(id,false);
    }

    public void enableUser(Long id) {
        userMapper.updateStatus(id,true);
    }

    /**
     * 分页获取用户信息，支持筛选和排序。
     *
     * @param page  当前页码。
     * @param limit 每页记录数。
     * @param sort  排序字段。
     * @param name  用户名（可选）。
     * @param email 邮箱（可选）。
     * @return 包含分页结果的对象。
     */
    public DraggableTableResult<UserInfo> getFilteredUsers(
            int page, int limit, String sort, String name, String email , String role , Boolean userStatus
    ) {
        // 计算分页偏移量
        int offset = (page - 1) * limit;

        sort = sort.equals("+id") ? "ASC" : "DESC";

        // 获取筛选后的用户数据
        List<UserInfo> users = userMapper.getFilteredUsers(sort, offset, limit, name, email , role , userStatus);

        // 查询总用户数（支持筛选）
        int total = userMapper.countFilteredUsers(name, email,role , userStatus);

        // 构建结果对象
        return com.david.hlp.SpringBootWork.blog.util.DraggableTableResult.<UserInfo>builder()
                .items(users)
                .total(total)
                .build();
    }

    public ArticleWrapper<UserInfo> updateUserInfo(Long id, UserInfoRequest userInfoRequest) {
        userMapper.updateUserInfo(userInfoRequest, id , userInfoRequest.getStatus());
        UserInfo userInfo = userMapper.getUserInfoById(id);
        return ArticleWrapper.<UserInfo>builder().article(userInfo).build();
    }
}