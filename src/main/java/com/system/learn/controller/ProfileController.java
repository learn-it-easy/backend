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
    public UserProfileDto getUserProfile() {
        return profileService.getUserProfile();
    }

    @PatchMapping
    public AuthResponseDto updateCurrentUserProfile(@Valid @RequestBody UserProfileChangeDto profileChangeDto) {
        return profileService.changeProfile(profileChangeDto);
    }

}
