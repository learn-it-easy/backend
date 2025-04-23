package com.system.learn.service;

import com.system.learn.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtContextService {


    private final JwtUtils jwtUtils;

    public JwtContextService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public Long getCurrentUserIdFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        return jwtUtils.getUserIdFromToken(token);
    }

    public String getLearningLanguageCodeFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        return jwtUtils.getLearningLanguageCode(token);
    }

    public String getNativeLanguageCodeFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        return jwtUtils.getNativeLanguageCode(token);
    }




    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtException("Invalid Authorization header");
        }
        return header.substring(7);
    }

}