package com.gateway.apigateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtils {

    @Value("${gateway.app.jwtSecret}")
    private String jwtSecret;

    @Value("${gateway.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
// 
    // public boolean validateToken(String token) {
    //     try {
    //         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }

    // public String generateJwtToken(String username) {
    //     return Jwts.builder()
    //             .setSubject(username)
    //             .setIssuedAt(new Date())
    //             .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
    //             .signWith(SignatureAlgorithm.HS512, jwtSecret)
    //             .compact();
    // }
  private Key key() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }
      public boolean validateJwtToken(String authToken) {
    try {
      System.out.println("hiiii"+ Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody());
      Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody();
     

      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
