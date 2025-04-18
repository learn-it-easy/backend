package com.system.learn.security;

import com.system.learn.entity.User;
import com.system.learn.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(UserRepository userRepository,
                            SecretKey secretKey,
                            @Value("${app.jwt.expiration}") long validityInMilliseconds) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String generateToken(String username, Long userId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        claims.put("userLearningLanguageCode", user.getLearningLanguage().getCode());
        claims.put("userNativeLanguageCode", user.getNativeLanguage().getCode());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

  public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}