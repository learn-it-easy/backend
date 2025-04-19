package com.system.learn.controller;

import com.system.learn.dto.AuthResponseDto;
import com.system.learn.dto.UserProfileChangeDto;
import com.system.learn.dto.UserProfileDto;
import com.system.learn.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public UserProfileDto getUserProfile(
            @RequestHeader("Authorization") String token,
            @CookieValue(value="lang", defaultValue = "ru") String lang) {

        return profileService.getUserProfile(token, lang);
    }

    @PatchMapping
    public AuthResponseDto updateCurrentUserProfile(
            @Valid @RequestBody UserProfileChangeDto profileChangeDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value="lang", defaultValue = "ru") String lang) {

        return profileService.changeProfile(profileChangeDto, token, lang);

    }

}
