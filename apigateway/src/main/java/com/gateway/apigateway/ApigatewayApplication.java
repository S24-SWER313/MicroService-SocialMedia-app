// package com.gateway.apigateway;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
// import reactor.core.publisher.Mono;

// @SpringBootApplication(exclude = SecurityAutoConfiguration.class)
// @EnableDiscoveryClient
// @RestController
// public class ApigatewayApplication {

//     public static void main(String[] args) {
//         SpringApplication.run(ApigatewayApplication.class, args);
//     }

//     @Bean
//     public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//         return builder.routes()
//                 .route(p -> p
//                         .path("/users/{userId}/posts/**")
//                         .filters(f -> f.circuitBreaker(config -> config
//                                 .setName("postsCircuitBreaker")
//                                 .setFallbackUri("forward:/fallback-error"))
								
//                 .addRequestHeader("Content-Type", "application/json")
//                 .addRequestHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYW5kYSIsImlhdCI6MTcxNzQxODg4OCwiZXhwIjoxNzE3NTA1Mjg4fQ.IPxlm604EBA8cAKaEgdmFVI_rAeZoe3tX2Mb_L9eFuA"))
                
//                         .uri("lb://postservice"))
//                 .route(p -> p
//                         .path("/users/**")
//                         .filters(f -> f.circuitBreaker(config -> config
//                                 .setName("usersCircuitBreaker")
//                                 .setFallbackUri("forward:/fallback-error")))
//                         .uri("lb://userservice"))
//                         .route(p -> p
//                         .path("/api/auth/**")
//                         .filters(f -> f.circuitBreaker(config -> config
//                                 .setName("usersCircuitBreaker")
//                                 .setFallbackUri("forward:/fallback-error")))
//                         .uri("lb://userservice"))
//                 .build();
//     }

//     @RequestMapping("/fallback-error")
//     public Mono<String> fallback() {
//         return Mono.just("A problem has occurred, please try again later.");
//     }
// }

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
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ApigatewayApplication {

//     @Autowired
//     private JwtAuthenticationFilter jwtAuthenticationFilter;

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        return builder.routes()
                .route(p -> p
                        .path("/users/1/posts/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .circuitBreaker(config -> config
                                        .setName("postsCircuitBreaker")
                                        .setFallbackUri("forward:/fallback-error")).addRequestHeader("Authorization", "Bearer"+JwtAuthenticationFilter.too)
                                .addRequestHeader("Content-Type", "application/json"))
                        .uri("lb://postservice"))
                .route(p -> p
                        .path("/users/**")
                        .filters(f -> f
                                .filter(jwtAuthenticationFilter)
                                .circuitBreaker(config -> config
                                        .setName("usersCircuitBreaker")
                                        .setFallbackUri("forward:/fallback-error")).addRequestHeader("Authorization", "Bearer"+JwtAuthenticationFilter.too))
                        .uri("lb://userservice"))
                .route(p -> p
                        .path("/api/auth/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("authCircuitBreaker")
                                .setFallbackUri("forward:/fallback-error")))
                        .uri("lb://userservice"))
                .build();
    }

    @RequestMapping("/fallback-error")
    public Mono<String> fallback() {
        return Mono.just("A problem has occurred, please try again later.");
    }
}
