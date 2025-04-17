package com.system.learn.service;

import com.system.learn.dto.AuthResponseDto;
import com.system.learn.dto.UserProfileChangeDto;
import com.system.learn.dto.UserProfileDto;
import com.system.learn.entity.User;
import com.system.learn.repository.LanguageRepository;
import com.system.learn.repository.UserRepository;
import com.system.learn.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    public final UserRepository userRepository;
    private final SecurityContextService securityContextService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;
    private final UserAuthService userAuthService;
    private final LanguageService languageService;

    public ProfileService(UserRepository userRepository, SecurityContextService securityContextService, PasswordEncoder passwordEncoder, UserValidationService userValidationService, UserAuthService userAuthService, JwtTokenProvider jwtTokenProvider, LanguageRepository languageRepository, LanguageService languageService) {
        this.userRepository = userRepository;
        this.securityContextService = securityContextService;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
        this.userAuthService = userAuthService;
        this.languageService = languageService;
    }

    public AuthResponseDto changeProfile(UserProfileChangeDto profileChangeDto) {

        Long currentUserId = securityContextService.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (profileChangeDto.getUsername() != null) {
            userValidationService.validateUserByUsername(profileChangeDto.getUsername(), currentUserId);
        }
        if (profileChangeDto.getEmail() != null) {
            userValidationService.validateUserByEmail(profileChangeDto.getEmail(), currentUserId);
        }


        try {

            if (profileChangeDto.getEmail() != null && !profileChangeDto.getEmail().equals(user.getEmail())) {
                user.setEmail(profileChangeDto.getEmail());
            }
            if (profileChangeDto.getUsername() != null && !profileChangeDto.getUsername().equals(user.getUsername())) {
                user.setUsername(profileChangeDto.getUsername());
            }
            if (profileChangeDto.getPassword() != null && !profileChangeDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(profileChangeDto.getPassword()));
            }
            if (profileChangeDto.getLearningLanguageId() != null && !profileChangeDto.getLearningLanguageId().equals(user.getLearningLanguage())) {
                languageService.setLanguage(user, profileChangeDto.getLearningLanguageId());
            }

            userRepository.save(user);

            return userAuthService.getToken(currentUserId, user.getUsername(), profileChangeDto.getPassword());

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Не удалось изменить профиль");
        }

    }

    public UserProfileDto getUserProfile() {

        Long currentUserId = securityContextService.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getLearningLanguage().getId()
        );

    }

}
