package com.david.hlp.SpringBootWork.system.auth.config;

import com.david.hlp.SpringBootWork.system.auth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    /**
     * 获取当前认证的用户详细信息
     *
     * @return 当前用户的 UserDetails 实例
     */
    protected User getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户的用户名
     *
     * @return 当前用户的用户名
     */
    protected String getCurrentUsername() {
        User user = getCurrentUserDetails();
        return user != null ? user.getUsername() : null;
    }

    protected Integer getCurrentUserId() {
        User user = getCurrentUserDetails();
        return user != null ? user.getId() : null;
    }

}


