package com.system.learn.controller;

import com.system.learn.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
public class UtilController {

    private final JwtUtils jwtUtils;

    public UtilController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        return jwtUtils.validateToken(authHeader);
    }
}