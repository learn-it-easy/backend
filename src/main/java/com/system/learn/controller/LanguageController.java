package com.system.learn.controller;

import com.system.learn.dto.LanguageDto;
import com.system.learn.service.LanguageService;
import com.system.learn.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {


    private final LanguageService languageService;
    private final CookieUtils cookieUtils;

    public LanguageController(LanguageService languageService, CookieUtils cookieUtils) {
        this.languageService = languageService;
        this.cookieUtils = cookieUtils;
    }


    @GetMapping("/all")
    public List<LanguageDto> getAllLanguages() {
            return languageService.getAllLanguages();
        }


    @GetMapping("/language-cookie")
    public ResponseEntity<?> getCookie(
            @RequestHeader("Authorization") String token,
            HttpServletResponse response){

        cookieUtils.addLanguageCookies(response, token);
        return ResponseEntity.ok().build();
    }
}
