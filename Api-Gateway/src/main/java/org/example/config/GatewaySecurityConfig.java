package org.example.config;

import jakarta.annotation.PostConstruct;
import org.example.security.JwtAuthenticationManager;
import org.example.security.JwtSecurityContextRepository;
import org.example.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@EnableWebFluxSecurity
@Configuration
public class GatewaySecurityConfig {

    @PostConstruct
    public void checkLoaded() {
        System.out.println("✅ GatewaySecurityConfig 로드됨");
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager(JwtUtil jwtUtil) {
        return new JwtAuthenticationManager(jwtUtil);
    }
    @Bean
    public JwtSecurityContextRepository jwtSecurityContextRepository(JwtAuthenticationManager manager) {
        return new JwtSecurityContextRepository(manager);
    }



    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtUtil jwtUtil) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // ✅ preflight 허용
                        .pathMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/employee/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/employee/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/employee-attendance/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/api/attendance/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/attendance/**").authenticated()
                        .anyExchange().authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authenticationManager(jwtAuthenticationManager(jwtUtil))
                .securityContextRepository(jwtSecurityContextRepository(jwtAuthenticationManager(jwtUtil)))
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
