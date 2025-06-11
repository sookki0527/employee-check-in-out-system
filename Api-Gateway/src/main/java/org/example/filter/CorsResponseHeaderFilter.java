package org.example.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Component
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(CorsResponseHeaderFilter.class);
    public CorsResponseHeaderFilter() {
        System.out.println("✅ CorsResponseHeaderFilter Bean created");
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("🔥 preflight is gateway");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        log.info("🔥 CorsResponseHeaderFilter triggered for {}", request.getURI());
        response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Allow-Headers", "*");


        if (request.getMethod() == HttpMethod.OPTIONS) {
            System.out.println("🔥 preflight CORS handled at gateway");
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 높은 우선순위
    }
}
