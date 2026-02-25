package daulspring.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

  private final JwyAuthFilter jwyAuthFilter;

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        /**
         *RouteSpec의 주요 메서드
         * .path("/auth/**") : 요청 경로 매칭
         * .filters(f -> ...) :해당 라우트에 적용할 필터 등록
         * .id("my-route") : 라우트 ID 명시적으로 지정 (생략 가능)
         * .order(10) : 라우트 매칭 순서
         * .host : ("www.google.com") 호스트 기반 라우팅
         */

        // Auth Service (필터 적용 안 함)
        .route("auth-service", r -> r
            .path("/auth/**")
            .uri("http://localhost:1001"))
        
        // User Service (필터 적용)
        .route("user-service", r -> r
            .path("/users/**")
            .filters(f -> f.filter(jwyAuthFilter))
            .uri("http://localhost:1002"))
        
        // Blog Service (필터 적용)
        .route("blog-service", r -> r
            .path("/api/**")
            .filters(f -> f.filter(jwyAuthFilter))
            .uri("http://localhost:1003"))
        
        .build();
  }
}