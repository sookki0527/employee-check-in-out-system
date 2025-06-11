package org.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("employee-service", r -> r.path("/api/employee/**")
                        .filters(f -> f.rewritePath("/api/employee/(?<segment>.*)", "/employee/${segment}"))
                        .uri("http://employee-service:8081"))
                .route("employee-auth", r -> r.path("/api/auth/**")
                        .filters(f -> f.rewritePath("/api/auth/(?<segment>.*)", "/auth/${segment}"))
                        .uri("http://employee-service:8081"))
                .route("employee-attendance", r -> r.path("/api/employee-attendance/**")
                        .filters(f -> f.rewritePath("/api/employee-attendance/(?<segment>.*)", "/employee-attendance/${segment}"))
                        .uri("http://employee-service:8081"))
                .route("attendance-service", r -> r.path("/api/attendance/**")
                        .filters(f -> f.rewritePath("/api/attendance/(?<segment>.*)", "/attendance/${segment}"))
                        .uri("http://attendance-service:8082"))
                .build();
    }
}
