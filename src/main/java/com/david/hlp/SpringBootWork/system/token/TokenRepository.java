package com.david.hlp.SpringBootWork.system.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Token 仓库接口。
 * <p>
 * 提供对 Token 实体的数据库访问操作。
 * <p>
 * 继承自 JpaRepository，可直接使用常见的 CRUD 方法。
 */
public interface TokenRepository extends JpaRepository<Token, Integer> {

  /**
   * 查询指定用户的所有有效令牌。
   *
   * @param id 用户 ID。
   * @return 返回与指定用户关联的所有未过期或未撤销的令牌列表。
   */
  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);

  /**
   * 根据令牌内容查找令牌。
   *
   * @param token 令牌内容。
   * @return 返回匹配的令牌（如果存在）。
   */
  Optional<Token> findByToken(String token);
}
