package com.system.learn.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {
    private final SecretKey secretKey;

    public JwtUtils(SecretKey secretKey) {
        this.secretKey = secretKey;
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

}