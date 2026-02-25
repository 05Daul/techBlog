package daulspring.gatewayservice.config;

import daulspring.commonsecurity.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwyAuthFilter implements GatewayFilter {

  private final JwtProvider jwtProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    /**
     * exchange.getRequest() : 현재 요청 정보 (헤더, 경로, 메서드, 바디 등)
     * exchange.getResponse() : 현재 응답 정보 (상태코드, 헤더, 바디 등)
     * exchange.mutate() : 요청이나 응답을 새로 만들어서 수정할 때 사용
     */
    String path = exchange.getRequest().getPath().value();
    HttpMethod method = exchange.getRequest().getMethod();  // ← HTTP 메서드 가져오기

    log.debug("요청: {} {}", method, path);

    // 1. 인증 불필요 경로 -> 원본 그대로 다음 필터로 넘김
    if (isPublicPath(path,method)) {
      return chain.filter(exchange);
    }

    // 2. 공개 경로가 아닐시 Authorization 헤더에서 토큰 추출
    String token = extractToken(exchange.getRequest());

    // 3. 토큰 검증
    if (token == null || !jwtProvider.isValid(token)) {

      log.warn("인증 실패: {} {}", method, path);
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 401 응답 설정
      return exchange.getResponse().setComplete(); // Mono<Void> 반환 → 요청 끝내기
    }

    // 4. userId, role 추출
    Long userId = jwtProvider.getUserId(token);
    String role = jwtProvider.getRole(token);

    log.info("인증 성공: userId={}, role={}", userId, role);

    // 5. 헤더 추가하여 내부 서비스로 전달
    /// Spring WebFlux는 대부분 불변 객체. 그렇기에 한 번 만들어지면 내용을 바꿀 수 없다.
    /// mutate() 를 호출해서 새로운 복사본 생성 -> 수정, 복사본에 변경사항을 적용.
    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
        .header("X-User-Id", String.valueOf(userId))
        .header("X-User-Role", role)
        .build();

    return chain.filter(exchange.mutate().request(modifiedRequest).build());
  }

  private boolean isPublicPath(String path, HttpMethod method) {

    // 회원가입, 로그인, 토큰 재발급
    if (path.startsWith("/auth/signup") ||
        path.startsWith("/auth/login") ||
        path.startsWith("/auth/refresh")) {
      return true;
    }

    // 게시글 조회만 허용 (GET /api/posts/*)
    if (path.startsWith("/api/posts/") && HttpMethod.GET.equals(method)) {
      return true;
    }

    return false;
  }
  private String extractToken(ServerHttpRequest request) {
    String bearer = request.getHeaders().getFirst("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
