package com.system.learn.configuration;

import com.system.learn.repository.UserRepository;
import com.system.learn.security.JwtTokenProvider;
import com.system.learn.utils.JwtUtils;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpirationMs;
    JwtTokenProvider jwtTokenProvider;
    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(SecretKey jwtSecretKey, UserRepository userRepository) {
        return  jwtTokenProvider = new JwtTokenProvider(userRepository, jwtSecretKey, jwtExpirationMs);
    }

    @Bean
    public JwtUtils jwtUtils(SecretKey jwtSecretKey) {
        return new JwtUtils(jwtSecretKey, jwtTokenProvider);
    }
}