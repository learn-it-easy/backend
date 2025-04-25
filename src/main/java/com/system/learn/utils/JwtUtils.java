package com.system.learn.utils;

import com.system.learn.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {
    private final SecretKey secretKey;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtUtils(SecretKey secretKey, JwtTokenProvider jwtTokenProvider) {
        this.secretKey = secretKey;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String cleanToken(String token) {
        if (token == null) {
            throw new JwtException("Token is null");
        }
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }

    public String getLearningLanguageCode(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userLearningLanguageCode", String.class);
    }

    public String getNativeLanguageCode(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userNativeLanguageCode", String.class);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    public ResponseEntity<?> validateToken (String authHeader){

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid authorization header");
        }

        String token = cleanToken(authHeader);

        boolean isValid = jwtTokenProvider.validateToken(token);

        if (isValid) {
            return ResponseEntity.ok().body("Token is valid");
        } else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}