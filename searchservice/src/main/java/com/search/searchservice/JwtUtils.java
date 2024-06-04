package com.search.searchservice;


// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.UnsupportedJwtException;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import java.security.Key;

// @Component
// public class JwtUtils {

//     @Value("${mypost.app.jwtSecret}")
//     private String jwtSecret;

//     private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

//     private Key key() {
//       return Keys.hmacShaKeyFor(jwtSecret.getBytes());
//     }
//     public String getUserNameFromJwtToken(String token) {
//       System.out.println("hiknkkn ");
//       return Jwts.parserBuilder().setSigningKey(key()).build()
//                  .parseClaimsJws(token).getBody().getSubject();
//     }
  

//     public boolean validateJwtToken(String authToken) {
//         try {
//             //Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody();
//             System.out.println("Token claims: " );
//             return true;
//         } catch (MalformedJwtException e) {
//             logger.error("Invalid JWT token: {}", e.getMessage());
//         } catch (ExpiredJwtException e) {
//             logger.error("JWT token is expired: {}", e.getMessage());
//         } catch (UnsupportedJwtException e) {
//             logger.error("JWT token is unsupported: {}", e.getMessage());
//         } catch (IllegalArgumentException e) {
//             logger.error("JWT claims string is empty: {}", e.getMessage());
//         }

//         return false;
//     }
// }


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

@Component
public class JwtUtils {

    @Value("${mypost.app.jwtSecret}")
    private String jwtSecret;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            System.out.println("iam in the validate ");
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("JWT signature does not match locally computed signature: {}", e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
      System.out.println("his");
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
        return claims.getBody().getSubject();
    }
}
