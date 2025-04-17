package com.system.learn.controller;

import com.system.learn.dto.LanguageDto;
import com.system.learn.service.LanguageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {


    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }


    @GetMapping("/all")
    public List<LanguageDto> getAllLanguages() {
            return languageService.getAllLanguages();
        }
}
