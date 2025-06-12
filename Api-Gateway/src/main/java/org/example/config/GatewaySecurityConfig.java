package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

public class GatewaySecurityConfig {
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> {}) // global CORS 연동
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .build();  // ✅ 여기까지만 설정하면 충분함
    }
}
