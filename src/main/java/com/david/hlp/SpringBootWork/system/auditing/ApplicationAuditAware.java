package com.david.hlp.SpringBootWork.system.auditing;

import com.david.hlp.SpringBootWork.system.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 实现 AuditorAware 接口，用于提供当前操作用户的信息。
 *
 * 此类主要用于 Spring Data JPA 的审计功能，
 * 自动填充实体中的 `@CreatedBy` 和 `@LastModifiedBy` 字段。
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {

    /**
     * 获取当前审计用户的 ID。
     *
     * 此方法从 Spring Security 的上下文中获取当前用户的认证信息，
     * 并提取用户 ID。如果用户未登录或为匿名用户，则返回 `Optional.empty()`。
     *
     * @return 包含当前用户 ID 的 Optional 对象。如果用户未登录或为匿名用户，则返回 Optional.empty()。
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        // 从 SecurityContextHolder 获取当前的认证信息
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // 验证认证信息是否存在，并检查用户是否已认证且不是匿名用户
        if (authentication == null || // 没有认证信息
                !authentication.isAuthenticated() || // 用户未认证
                authentication instanceof AnonymousAuthenticationToken // 用户为匿名用户
        ) {
            // 返回空值，表示无可用的审计用户
            return Optional.empty();
        }

        // 获取认证主体，并尝试转换为自定义的 User 对象
        User userPrincipal = (User) authentication.getPrincipal();

        // 提取用户 ID 并封装为 Optional 对象返回
        return Optional.ofNullable(userPrincipal.getId());
    }
}