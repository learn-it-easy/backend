package com.system.learn.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {
    private final SecretKey secretKey;

    public JwtUtils(SecretKey secretKey) {
        this.secretKey = secretKey;
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

}