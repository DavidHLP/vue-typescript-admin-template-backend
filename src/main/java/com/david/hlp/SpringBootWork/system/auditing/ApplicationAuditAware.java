package com.david.hlp.SpringBootWork.system.auditing;

import com.david.hlp.SpringBootWork.system.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 实现AuditorAware接口，用于提供当前操作用户的信息，
 * 主要用于Spring Data JPA的审计功能，自动填充`@CreatedBy`和`@LastModifiedBy`字段。
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {
    /**
     * 获取当前审计用户的ID。
     *
     * @return 包含当前用户ID的Optional对象。如果用户未登录或为匿名用户，则返回Optional.empty()。
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        // 从SecurityContextHolder获取当前的认证信息
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // 如果没有认证信息、用户未认证，或用户为匿名用户，返回Optional.empty()
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        // 获取认证主体，并转换为自定义的User对象
        User userPrincipal = (User) authentication.getPrincipal();

        // 返回用户的ID，可能为空
        return Optional.ofNullable(userPrincipal.getId());
    }
}
