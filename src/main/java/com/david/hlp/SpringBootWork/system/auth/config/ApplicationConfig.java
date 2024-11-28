package com.david.hlp.SpringBootWork.system.auth.config;

import com.david.hlp.SpringBootWork.system.auditing.ApplicationAuditAware;
import com.david.hlp.SpringBootWork.system.auth.Repository.UserRepository;
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
   * 用于根据用户名（邮箱）加载用户的详细信息。
   *
   * @return UserDetailsService 实例。
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // 抛出异常表示用户不存在
  }

  /**
   * 定义 AuthenticationProvider Bean。
   *
   * 使用 DaoAuthenticationProvider 实现用户认证，并设置用户详情服务和密码编码器。
   *
   * @return AuthenticationProvider 实例。
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService()); // 设置用户详情服务
    authProvider.setPasswordEncoder(passwordEncoder()); // 设置密码编码器
    return authProvider;
  }

  /**
   * 定义 AuditorAware Bean。
   *
   * 用于 Spring Data JPA 审计功能，记录实体的创建者和修改者。
   *
   * @return AuditorAware 实例。
   */
  @Bean
  public AuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware();
  }

  /**
   * 定义 AuthenticationManager Bean。
   *
   * 提供用于用户认证的核心管理器，支持多个认证方式。
   *
   * @param config AuthenticationConfiguration 实例。
   * @return AuthenticationManager 实例。
   * @throws Exception 如果获取 AuthenticationManager 失败。
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager(); // 从配置中获取 AuthenticationManager
  }

  /**
   * 定义 PasswordEncoder Bean。
   *
   * 使用 BCrypt 加密算法对密码进行加密和验证。
   *
   * @return PasswordEncoder 实例。
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
