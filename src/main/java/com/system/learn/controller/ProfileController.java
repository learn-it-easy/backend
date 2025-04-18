package com.system.learn.controller;

import com.system.learn.dto.AuthResponseDto;
import com.system.learn.dto.UserProfileChangeDto;
import com.system.learn.dto.UserProfileDto;
import com.system.learn.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public UserProfileDto getUserProfile(HttpServletRequest request,
                                         @CookieValue(value="lang", defaultValue = "ru") String lang) {

        Locale locale = Locale.forLanguageTag(lang);
        return profileService.getUserProfile(request, locale);
    }

    @PatchMapping
    public AuthResponseDto updateCurrentUserProfile(@Valid @RequestBody UserProfileChangeDto profileChangeDto,
                                                    HttpServletRequest request,
                                                    @CookieValue(value="lang", defaultValue = "ru") String lang) {

        Locale locale = Locale.forLanguageTag(lang);
        return profileService.changeProfile(profileChangeDto, request, locale);

    }

}
