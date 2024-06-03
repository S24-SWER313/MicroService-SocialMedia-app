package com.gateway.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import reactor.core.publisher.Mono;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableDiscoveryClient
@RestController
public class ApigatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/users/{userId}/posts/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("postsCircuitBreaker")
                                .setFallbackUri("forward:/fallback-error"))
								
                .addRequestHeader("Content-Type", "application/json"))
                        .uri("lb://postservice"))
                .route(p -> p
                        .path("/users/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("usersCircuitBreaker")
                                .setFallbackUri("forward:/fallback-error")))
                        .uri("lb://userservice"))
                .build();
    }

    @RequestMapping("/fallback-error")
    public Mono<String> fallback() {
        return Mono.just("A problem has occurred, please try again later.");
    }
}
