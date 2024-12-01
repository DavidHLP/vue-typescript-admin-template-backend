package com.david.hlp.SpringBootWork.system.auth;

import com.david.hlp.SpringBootWork.system.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

/**
 * 基础控制器类。
 *
 * 提供一些通用的方法，用于获取当前登录用户的相关信息。
 */
@Controller
public class BaseController {

    /**
     * 获取当前认证的用户详细信息。
     *
     * 通过 Spring Security 的 SecurityContextHolder 获取当前认证对象，
     * 并从中提取用户详细信息。如果认证对象不存在或认证未通过，则返回 null。
     *
     * @return 当前用户的 UserDetails 实例（强制转换为 User 类型），如果未认证则返回 null。
     */
    protected User getCurrentUserDetails() {
        // 获取当前认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 验证认证对象是否有效，并判断认证主体是否为 UserDetails 实例
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // 将认证主体强制转换为 User 并返回
            return (User) authentication.getPrincipal();
        }
        // 如果认证无效或主体不是 UserDetails 实例，返回 null
        return null;
    }

    /**
     * 获取当前用户的用户名。
     *
     * 调用 getCurrentUserDetails() 方法获取当前用户的详细信息，
     * 并提取用户名。如果未获取到用户信息，则返回 null。
     *
     * @return 当前用户的用户名，如果未认证则返回 null。
     */
    protected String getCurrentUsername() {
        // 获取当前用户详细信息
        User user = getCurrentUserDetails();
        // 返回用户名，或在用户为 null 时返回 null
        return user != null ? user.getUsername() : null;
    }

    /**
     * 获取当前用户的用户 ID。
     *
     * 调用 getCurrentUserDetails() 方法获取当前用户的详细信息，
     * 并提取用户 ID。如果未获取到用户信息，则返回 null。
     *
     * @return 当前用户的用户 ID，如果未认证则返回 null。
     */
    protected Integer getCurrentUserId() {
        // 获取当前用户详细信息
        User user = getCurrentUserDetails();
        // 返回用户 ID，或在用户为 null 时返回 null
        return user != null ? user.getId() : null;
    }
}
