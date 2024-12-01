package com.david.hlp.SpringBootWork.system.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 服务类。
 *
 * 提供生成、解析和验证 JWT 的核心功能。
 */
@Service
public class JwtService {

  // 从配置文件中加载秘钥（Base64 编码格式）
  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  // 从配置文件中加载 JWT 的有效期（单位：毫秒）
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  // 从配置文件中加载刷新令牌的有效期（单位：毫秒）
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  /**
   * 从令牌中提取用户名。
   *
   * @param token JWT 令牌。
   * @return 提取到的用户名。
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject); // 提取 "subject" 声明，通常是用户名
  }

  /**
   * 从令牌中提取一个指定的声明信息。
   *
   * @param token          JWT 令牌。
   * @param claimsResolver 声明解析函数。
   * @param <T>            声明的类型。
   * @return 提取到的声明信息。
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token); // 获取令牌中的所有声明
    return claimsResolver.apply(claims); // 通过解析函数获取指定的声明
  }

  /**
   * 生成 JWT 令牌（无额外声明）。
   *
   * @param userDetails 用户详细信息。
   * @return 生成的 JWT 令牌。
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails); // 使用空的 Map 表示无额外声明
  }

  /**
   * 生成带额外声明的 JWT 令牌。
   *
   * @param extraClaims 额外声明。
   * @param userDetails 用户详细信息。
   * @return 生成的 JWT 令牌。
   */
  public String generateToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration); // 使用配置中的默认有效期
  }

  /**
   * 生成刷新令牌。
   *
   * @param userDetails 用户详细信息。
   * @return 生成的刷新令牌。
   */
  public String generateRefreshToken(UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration); // 使用刷新令牌的有效期
  }

  /**
   * 构建 JWT 令牌。
   *
   * @param extraClaims 额外声明。
   * @param userDetails 用户详细信息。
   * @param expiration  令牌的有效期（单位：毫秒）。
   * @return 生成的 JWT 令牌。
   */
  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    return Jwts
            .builder()
            .setClaims(extraClaims) // 设置额外声明
            .setSubject(userDetails.getUsername()) // 设置主题，通常为用户名
            .setIssuedAt(new Date(System.currentTimeMillis())) // 设置签发时间
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) // 使用 HMAC-SHA256 签名
            .compact(); // 构建并返回 JWT
  }

  /**
   * 验证令牌是否有效。
   *
   * @param token       JWT 令牌。
   * @param userDetails 用户详细信息。
   * @return 如果令牌有效且未过期，则返回 true。
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token); // 提取用户名
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // 验证用户名一致性和过期状态
  }

  /**
   * 检查令牌是否已过期。
   *
   * @param token JWT 令牌。
   * @return 如果令牌已过期，则返回 true。
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date()); // 判断过期时间是否早于当前时间
  }

  /**
   * 提取令牌的过期时间。
   *
   * @param token JWT 令牌。
   * @return 令牌的过期时间。
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration); // 提取 "expiration" 声明
  }

  /**
   * 提取令牌中的所有声明。
   *
   * @param token JWT 令牌。
   * @return 提取到的所有声明。
   */
  private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey()) // 设置签名秘钥
            .build()
            .parseClaimsJws(token) // 解析令牌
            .getBody(); // 获取声明体
  }

  /**
   * 获取签名秘钥。
   *
   * @return 签名秘钥对象。
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // 使用 Base64 解码秘钥
    return Keys.hmacShaKeyFor(keyBytes); // 使用 HMAC-SHA 算法生成秘钥对象
  }
}