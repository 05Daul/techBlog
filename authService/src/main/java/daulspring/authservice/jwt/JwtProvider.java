package daulspring.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private SecretKey key; //대칭키 암호화 방식
  // Key타입은 비대칭키(공개키/개인키) + 대칭키 포함

  private static final long ACCESS_TOKEN_EXPIRE  = 1000L * 60 * 60;           // 1시간
  private static final long REFRESH_TOKEN_EXPIRE = 1000L * 60 * 60 * 24 * 7;  // 7일


  public JwtProvider(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    // secret문자열 -> byte 배열 전환 후 -> HMAC-SHA 알고리즘 키 객체로
    /// 나중에 비대칭키로 변경 고려하기
  }

  public String createAccessToken(Long userId, String roleName) {
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("role", roleName)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
        .signWith(key, SignatureAlgorithm.HS256)// 해당 알고리즘으로 서명
        .compact();
  }

  public String createRefreshToken(Long userId) {
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE))
        .signWith(key, SignatureAlgorithm.HS256) // 해당 알고리즘으로 서명
        .compact();
  }

  public Claims parseClaims(String token) { // 페이로더 분석
    return Jwts.parserBuilder()
        .setSigningKey(key) // 검증할 키 설정, 헤더에 있는 알고리즘을 확인 먼저함. 그래서 위처럼 작성할 필요X.
        .build()
      .parseClaimsJws(token)   // 1) header/payload Base64Url 디코딩 → JSON 변환
                               // 2) header의 alg 확인
                               // 3) (header + "." + payload)를 같은 알고리즘 + key로 다시 "서명값 계산"
                               // 4) 계산한 서명값과 token의 signature 비교
                               // 5) exp 등 유효성 검사 후 통과하면 Claims 반환
        .getBody();
  }

  public boolean isValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Long getUserId(String token) {
    return Long.parseLong(parseClaims(token).getSubject());
  }

  public String getRole(String token) {
    return parseClaims(token).get("role", String.class);
  }
}