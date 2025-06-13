package org.example.filter;

import org.example.util.JwtUtil;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtUtil jwtUtil;
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;

    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final String token = this.getAuthHeader(request).substring(7);

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("🔥 [JwtAuthFilter] Incoming request: " + exchange.getRequest().getMethod() + " " + path);

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            System.out.println("✅ [JwtAuthFilter] Preflight request passed through");
            return chain.filter(exchange);
        }

        if (path.startsWith("/api/auth/register") || path.startsWith("/api/auth/login")) {
            System.out.println("🟢 [JwtAuthFilter] Public path, skipping token check");
            return chain.filter(exchange);
        }

        if(!jwtUtil.validateToken(token)){
            System.out.println("❌ token: "+ token);
            System.out.println("❌ [JwtAuthFilter] Invalid JWT token");
            return this.onError(exchange, HttpStatus.FORBIDDEN);
        }
        System.out.println("✅ [JwtAuthFilter] Valid JWT token, proceeding");
        return chain.filter(exchange);
    }

    private String getAuthHeader(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
        if (authHeaders.isEmpty()) return "";
        return authHeaders.get(0);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
