package com.system.learn.controller;

import com.system.learn.dto.auth.AuthResponseDto;
import com.system.learn.dto.profile.UserProfileChangeDto;
import com.system.learn.dto.profile.UserProfileDto;
import com.system.learn.service.ProfileService;
import com.system.learn.utils.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final CookieUtils cookieUtils;

    public ProfileController(ProfileService profileService, CookieUtils cookieUtils) {
        this.profileService = profileService;
        this.cookieUtils = cookieUtils;
    }

    @GetMapping
    public UserProfileDto getUserProfile(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return profileService.getUserProfile(token, lang);
    }

    @PatchMapping
    public ResponseEntity<AuthResponseDto> updateCurrentUserProfile(
            @Valid @RequestBody UserProfileChangeDto profileChangeDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value = CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            HttpServletResponse response) {

        AuthResponseDto dto = profileService.changeProfile(profileChangeDto, token, lang);

        cookieUtils.changeLanguageCookies(
                response,
                profileChangeDto.getNativeLanguageId(),
                profileChangeDto.getLearningLanguageId(),
                lang
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getHeader(HttpHeaders.SET_COOKIE))
                .body(dto);
    }

}
