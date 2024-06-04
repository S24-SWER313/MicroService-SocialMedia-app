package com.search.searchservice;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtils.validateJwtToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
//      private String parseJwt(HttpServletRequest request) {
//     String headerAuth = request.getHeader("Authorization");

//     if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//       return headerAuth.substring(7);
//     }

// }
}

// In JwtUtils class
// public boolean validateJwtToken(String authToken) {
//     try {
//         Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
//         return true;
//     } catch (Exception e) {
//         logger.error("JWT validation error: {}", e.getMessage());
//     }
//     return false;
// }

// // In JwtAuthenticationFilter class
// @Override
// protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//     String authHeader = request.getHeader("Authorization");

//     if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//         logger.warn("Missing or invalid Authorization header");
//         response.setStatus(HttpStatus.UNAUTHORIZED.value());
//         return;
//     }

//     String token = authHeader.substring(7);

//     if (!jwtUtils.validateJwtToken(token)) {
//         logger.warn("Invalid JWT token");
//         response.setStatus(HttpStatus.UNAUTHORIZED.value());
//         return;
//     }

//     // Log claims
//     Claims claims = Jwts.parserBuilder().setSigningKey(jwtUtils.key()).build().parseClaimsJws(token).getBody();
//     logger.info("JWT claims: {}", claims);

//     filterChain.doFilter(request, response);
// }
// }