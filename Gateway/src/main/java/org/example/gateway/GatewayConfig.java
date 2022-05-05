package org.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.AddRequestParameterGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("translator-manager", r -> r.path("/translator/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8085/"))
                .route("authentication-manager", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:9090/"))
                .route("core", r -> r.path("/core/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8080/"))
                .build();
    }
}
