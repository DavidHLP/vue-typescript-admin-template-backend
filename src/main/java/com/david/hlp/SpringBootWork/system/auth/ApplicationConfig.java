package com.david.hlp.SpringBootWork.system.auth;

import com.david.hlp.SpringBootWork.system.auditing.ApplicationAuditAware;
import com.david.hlp.SpringBootWork.system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 应用程序配置类。
 *
 * 提供 Spring Security 的核心配置和审计功能的相关 Bean 定义。
 */
@Configuration
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class ApplicationConfig {

  // 用户存储库，用于从数据库获取用户信息
  private final UserRepository repository;

  /**
   * 定义 UserDetailsService Bean。
   *
   * UserDetailsService 用于从数据库加载用户信息，提供认证所需的用户详细数据。
   *
   * @return UserDetailsService 实例。
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // 如果找不到用户，则抛出异常
  }

  /**
   * 定义 AuthenticationProvider Bean。
   *
   * DaoAuthenticationProvider 是 Spring Security 提供的一种认证提供器，
   * 它支持基于数据库的用户认证。
   *
   * @return AuthenticationProvider 实例。
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService()); // 设置用户详情服务
    authProvider.setPasswordEncoder(passwordEncoder()); // 设置密码编码器，用于密码加密和验证
    return authProvider;
  }

  /**
   * 定义 AuditorAware Bean。
   *
   * AuditorAware 用于审计实体的创建者和修改者。它与 Spring Data JPA 审计功能集成。
   *
   * @return AuditorAware 实例。
   */
  @Bean
  public AuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware(); // 自定义的审计功能实现类
  }

  /**
   * 定义 AuthenticationManager Bean。
   *
   * AuthenticationManager 是 Spring Security 的核心认证管理器，
   * 它用于管理用户认证，并支持多个认证提供器。
   *
   * @param config AuthenticationConfiguration 实例，包含应用程序的认证配置。
   * @return AuthenticationManager 实例。
   * @throws Exception 如果获取 AuthenticationManager 失败。
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager(); // 使用配置中的认证管理器实例
  }

  /**
   * 定义 PasswordEncoder Bean。
   *
   * PasswordEncoder 用于对用户密码进行加密和验证。
   * 这里使用的是 BCrypt 加密算法，它是一种安全的哈希算法，适合存储密码。
   *
   * @return PasswordEncoder 实例。
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 使用 BCrypt 算法进行密码加密
  }
}