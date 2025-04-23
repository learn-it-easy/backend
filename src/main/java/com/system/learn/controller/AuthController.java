package com.system.learn.controller;

import com.system.learn.dto.auth.AuthResponseDto;
import com.system.learn.dto.auth.LoginRequestDto;
import com.system.learn.dto.auth.UserRegistrationDto;
import com.system.learn.service.UserAuthService;
import com.system.learn.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthService userService;
    private final CookieUtils cookieUtils;

    @Autowired
    public AuthController(UserAuthService userService, CookieUtils cookieUtils) {
        this.userService = userService;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(
            @RequestBody UserRegistrationDto registrationDto,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            HttpServletResponse response) {

        AuthResponseDto token = userService.registerUser(registrationDto, lang);
        cookieUtils.addLanguageCookies(response, token.getToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            HttpServletResponse response) {

        AuthResponseDto token = userService.authenticateUser(loginRequestDto, lang);
        cookieUtils.addLanguageCookies(response, token.getToken());
        return ResponseEntity.ok(token);
    }

}