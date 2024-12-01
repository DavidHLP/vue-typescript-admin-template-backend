package com.david.hlp.SpringBootWork.system.Repository;

import com.david.hlp.SpringBootWork.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层接口。
 *
 * 描述：
 * <p>
 * - 提供对用户表 (User) 的基本 CRUD 操作。
 * <p>
 * - 定义了基于邮箱的自定义查询方法，用于按需查找用户。
 * <p>
 * - 继承自 JpaRepository，支持分页、排序和其他高级数据操作功能。
 * <p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  /**
   * 根据用户邮箱查询用户信息。
   *
   * 描述：
   * <p>
   * - 根据邮箱地址 (email) 查询数据库中的用户记录。
   * <p>
   * - 返回结果封装在 Optional 中，以便处理可能的空值情况。
   *
   * @param email 用户邮箱地址。
   * @return 包含用户信息的 Optional 对象。如果未找到用户，则返回一个空的 Optional。
   */
  Optional<User> findByEmail(String email);
}