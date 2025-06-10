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
                .route("attendance-service", r -> r.path("/attendance/**")
                        .uri("http://attendance-service:8082"))
                .route("employee-service", r-> r.path("/auth/**" , "/employee/**", "/employee-attendance/**")
                        .uri("http://employee-service:8081"))
                .build();
    }
}
