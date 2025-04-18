package com.system.learn.controller;

import com.system.learn.dto.AuthResponseDto;
import com.system.learn.dto.LoginRequestDto;
import com.system.learn.dto.UserRegistrationDto;
import com.system.learn.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthService userService;

    @Autowired
    public AuthController(UserAuthService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@RequestBody UserRegistrationDto registrationDto,
                                                        @CookieValue(value="lang", defaultValue = "ru") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        AuthResponseDto token = userService.registerUser(registrationDto, locale);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                 @CookieValue(value="lang", defaultValue = "ru") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        AuthResponseDto token = userService.authenticateUser(loginRequestDto, locale);
        return ResponseEntity.ok(token);
    }

}