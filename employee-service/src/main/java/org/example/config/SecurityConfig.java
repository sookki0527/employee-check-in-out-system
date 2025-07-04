package org.example.config;

import org.example.repository.EmployeeRepository;
import org.example.security.JwtRequestFilter;
import org.example.service.CustomUserDetailService;
import org.example.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailService = userDetailsService;
    }


    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, customUserDetailService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                .csrf(csrf -> csrf.disable())
                 .httpBasic(httpBasic -> httpBasic.disable())   // ✅ 꼭 필요
                 .formLogin(form -> form.disable())             // ✅ 꼭 필요
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employee-attendance/check-in").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employee-attendance/check-out").permitAll()
                        .anyRequest().authenticated()
                )
                 .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
            return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
