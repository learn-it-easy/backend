package com.system.learn.utils;

import com.system.learn.service.LanguageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Locale;

@Component
public class CookieUtils {
    public static final String NATIVE_LANG_COOKIE = "nativeLang";
    public static final String LEARNING_LANG_COOKIE = "learningLang";
    public static final String INTERFACE_LANG_COOKIE = "lang";
    public static final String DEFAULT_LANG_FOR_INTERFACE_COOKIE = "ru";

    private final JwtUtils jwtUtils;
    private final LanguageService languageService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.frontend.domain}")
    private String domain;
    private CookieUtils(JwtUtils jwtUtils, LanguageService languageService) {
        this.jwtUtils = jwtUtils;
        this.languageService = languageService;
    }

    public void addLanguageCookies(HttpServletResponse response, String token) {

        token = jwtUtils.cleanToken(token);

        String nativeLang = jwtUtils.getNativeLanguageCode(token);
        String learningLang = jwtUtils.getLearningLanguageCode(token);

        ResponseCookie nativeCookie = ResponseCookie.from(CookieUtils.NATIVE_LANG_COOKIE, nativeLang)
                .domain(domain)
                .path("/")          // Куки доступны для всех путей на домене
                .sameSite("Lax")    // Защита от CSRF-атак (разрешены GET-запросы извне)
                .httpOnly(false)    // Разрешаем доступ к кукам через JavaScript
                .secure(false)
                .maxAge(Duration.ofDays(1)) // Срок жизни на сутки
                .build();

        ResponseCookie learningCookie = ResponseCookie.from(CookieUtils.LEARNING_LANG_COOKIE, learningLang)
                .domain(domain)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, nativeCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, learningCookie.toString());
    }

    public void changeLanguageCookies(HttpServletResponse response,
                                   Long nativeLangId, Long learningLangId, String locale) {

        ResponseCookie nativeCookie = ResponseCookie.from(NATIVE_LANG_COOKIE, languageService.getNativeLanguageCodeById(nativeLangId, Locale.forLanguageTag(locale)))
                .domain(domain)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .build();

        ResponseCookie learningCookie = ResponseCookie.from(LEARNING_LANG_COOKIE, languageService.getLearningLanguageCodeById(learningLangId, Locale.forLanguageTag(locale)))
                .domain(domain)
                .path("/")
                .sameSite("Lax")
                .httpOnly(false)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, nativeCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, learningCookie.toString());
    }
}