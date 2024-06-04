package com.mypost.postservice;



// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
// import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
// import org.springframework.security.config.web.server.ServerHttpSecurity;
// import org.springframework.security.web.server.SecurityWebFilterChain;
// import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
// import org.springframework.security.web.server.context.ServerSecurityContextRepository;
// import org.springframework.web.server.ServerWebExchange;
// import org.springframework.web.server.WebFilter;
// import org.springframework.web.server.WebFilterChain;
// import reactor.core.publisher.Mono;

// @Configuration
// // @EnableSecurity
// public class SecurityConfig {

//     // @Value("${mypost.jwt.secret}")
//     // private String jwtSecret;

//     @Value("${mypost.app.jwtSecret}")
//     private String jwtSecret;
  



//     @Bean
//     public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//         return http
//             .csrf().disable()
//             .authorizeExchange(exchanges -> exchanges
//                 .pathMatchers("/public/**").permitAll()
//                 .anyExchange().authenticated()
//             )
     
//             .build();
//     }
//     @Bean
//     public WebFilter jwtAuthenticationWebFilter() {
//         return new WebFilter() {
//             @Override
//             public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//                 String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

//                 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                     exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                     return exchange.getResponse().setComplete();
//                 }

//                 String token = authHeader.substring(7);

//                 try {
//                     Claims claims = Jwts.parser()
//                             .setSigningKey(jwtSecret)
//                             .parseClaimsJws(token)
//                             .getBody();
//                     exchange.getAttributes().put("claims", claims);
//                 } catch (Exception e) {
//                     exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                     return exchange.getResponse().setComplete();
//                 }

//                 return chain.filter(exchange);
//             }
//         };
//     }
// }



// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

// @Configuration
// @EnableMethodSecurity
// public class SecurityConfig {

// @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// http
//  .csrf(csrf -> csrf.disable())
//  .authorizeHttpRequests(authorize -> authorize
// .anyRequest().authenticated()
// );
// //  .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

//  return http.build();
//  }
// }


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())

        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth ->
            auth.requestMatchers("/api/auth/**").permitAll()
            // .requestMatchers("/swagegr-ui.html").permitAll()
            // .requestMatchers("/swagegr-ui/**").permitAll()
            // .requestMatchers("/swagegr-ui.html").permitAll()
                .requestMatchers("/users/**").authenticated()
            .anyRequest().authenticated());

   

            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}

}
