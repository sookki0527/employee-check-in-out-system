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
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            HttpMethod method = exchange.getRequest().getMethod();
            String path = exchange.getRequest().getURI().getPath();
            System.out.println("🔥 [JwtAuthFilter] Incoming request: " + method + " " + path);
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                System.out.println("✅ [JwtAuthFilter] Preflight request passed through");
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("❌ [JwtAuthFilter] Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                System.out.println("❌ [JwtAuthFilter] Invalid JWT token");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            System.out.println("✅ [JwtAuthFilter] Valid JWT token, proceeding");
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 빈 설정 클래스 (필요하면 값 전달 가능)
    }
}
