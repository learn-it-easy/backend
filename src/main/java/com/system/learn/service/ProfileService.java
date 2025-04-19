package com.system.learn.service;

import com.system.learn.dto.AuthResponseDto;
import com.system.learn.dto.UserProfileChangeDto;
import com.system.learn.dto.UserProfileDto;
import com.system.learn.entity.User;
import com.system.learn.repository.LanguageRepository;
import com.system.learn.repository.UserRepository;
import com.system.learn.security.JwtTokenProvider;
import com.system.learn.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ProfileService {

    @Autowired
    private MessageSource messageSource;
    public final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;
    private final UserAuthService userAuthService;
    private final LanguageService languageService;

    public ProfileService(UserRepository userRepository, JwtUtils jwtUtils, UserService userService, PasswordEncoder passwordEncoder, UserValidationService userValidationService, UserAuthService userAuthService, JwtTokenProvider jwtTokenProvider, LanguageRepository languageRepository, LanguageService languageService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
        this.userAuthService = userAuthService;
        this.languageService = languageService;
    }

    public AuthResponseDto changeProfile(UserProfileChangeDto profileChangeDto,String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(token);

        Locale locale = Locale.forLanguageTag(lang);

        User user = userService.getUserById(currentUserId, lang);

        if (profileChangeDto.getUsername() != null) {
            userValidationService.validateUserByUsername(profileChangeDto.getUsername(), currentUserId, locale);
        }
        if (profileChangeDto.getEmail() != null) {
            userValidationService.validateUserByEmail(profileChangeDto.getEmail(), currentUserId, locale);
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
            if (profileChangeDto.getLearningLanguageId() != null && !profileChangeDto.getLearningLanguageId().equals(user.getLearningLanguage().getId())) {
                languageService.setLearningLanguage(user, profileChangeDto.getLearningLanguageId(), locale);
            }
            if (profileChangeDto.getNativeLanguageId() != null && !profileChangeDto.getNativeLanguageId().equals(user.getNativeLanguage().getId())) {
                languageService.setNativeLanguage(user, profileChangeDto.getNativeLanguageId(), locale);
            }

            userRepository.save(user);

            return userAuthService.getToken(currentUserId, user.getUsername(), profileChangeDto.getPassword(), lang);

        } catch (BadCredentialsException e) {
            throw new RuntimeException( messageSource.getMessage("error.user_can_not_change_profile", null, locale));
        }
 
    }

    public UserProfileDto getUserProfile(String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User user = userService.getUserById(currentUserId, lang);


        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getLearningLanguage().getId(),
                user.getNativeLanguage().getId()
        );

    }

}
