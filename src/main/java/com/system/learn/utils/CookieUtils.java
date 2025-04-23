package com.system.learn.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtils {
    public static final String NATIVE_LANG_COOKIE = "nativeLang";
    public static final String LEARNING_LANG_COOKIE = "learningLang";
    public static final String INTERFACE_LANG_COOKIE = "lang";
    public static final String DEFAULT_LANG_FOR_INTERFACE_COOKIE = "ru";

    private final JwtUtils jwtUtils;

    private CookieUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public void addLanguageCookies(HttpServletResponse response, String token) {

        token = jwtUtils.cleanToken(token);

        String nativeLang = jwtUtils.getNativeLanguageCode(token);
        String learningLang = jwtUtils.getLearningLanguageCode(token);

        ResponseCookie nativeCookie = ResponseCookie.from(CookieUtils.NATIVE_LANG_COOKIE, nativeLang)
                .path("/")          // Куки доступны для всех путей на домене
                .sameSite("Lax")    // Защита от CSRF-атак (разрешены GET-запросы извне)
                .httpOnly(false)    // Разрешаем доступ к кукам через JavaScript
                .maxAge(Duration.ofDays(60 * 60 * 24 * 365)) // Срок жизни на год
                .build();

        ResponseCookie learningCookie = ResponseCookie.from(CookieUtils.LEARNING_LANG_COOKIE, learningLang)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .maxAge(Duration.ofDays(60 * 60 * 24 * 365))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, nativeCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, learningCookie.toString());
    }
}