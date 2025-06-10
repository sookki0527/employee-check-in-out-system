package org.example.config;

import org.example.security.JwtAuthenticationManager;
import org.example.security.JwtSecurityContextRepository;
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
public class SecurityConfig {

    private final JwtAuthenticationManager authenticationManager;

    private final JwtSecurityContextRepository securityContextRepository;

    public SecurityConfig(JwtAuthenticationManager authenticationManager,
                          JwtSecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> {})  // WebFlux에서는 Customizer.withDefaults() 대신 빈 블록
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/auth/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/employee/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/employee/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/employee-attendance/**").authenticated()
                        .pathMatchers(HttpMethod.POST, "/attendance/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/attendance/**").authenticated()
                        .anyExchange().authenticated()
                )
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
