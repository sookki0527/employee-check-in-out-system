package org.example.filter;

import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getURI().getPath();
        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        System.out.println("🔥 [JwtAuthFilter] Incoming request: " + method + " " + path);
        throw new RuntimeException("🔥 필터 실행 확인용 에러");
//        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
//        }
//
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
//        }
//
//        String token = authHeader.substring(7);
//        if (!jwtUtil.validateToken(token)) {
//            return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
//        }
//
//        // Optional: 사용자 정보 Header로 추가
//        String username = jwtUtil.extractUsername(token);
//        ServerHttpRequest mutatedRequest = request.mutate()
//                .header("X-User-Name", username)
//                .build();
//
//        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // 우선순위 높게
    }
//    @Override
//    public GatewayFilter apply(Config config) {
//
//        return (exchange, chain) -> {
//            HttpMethod method = exchange.getRequest().getMethod();
//            String path = exchange.getRequest().getURI().getPath();
//            System.out.println("🔥 [JwtAuthFilter] Incoming request: " + method + " " + path);
//            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//                System.out.println("✅ [JwtAuthFilter] Preflight request passed through");
//                return chain.filter(exchange);
//            }
//
//            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                System.out.println("❌ [JwtAuthFilter] Missing or invalid Authorization header");
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//
//            String token = authHeader.substring(7);
//            if (!jwtUtil.validateToken(token)) {
//                System.out.println("❌ [JwtAuthFilter] Invalid JWT token");
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//            System.out.println("✅ [JwtAuthFilter] Valid JWT token, proceeding");
//            return chain.filter(exchange);
//        };
//    }

//    public static class Config {
//        // 빈 설정 클래스 (필요하면 값 전달 가능)
//    }
}
