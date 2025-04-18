package com.system.learn.service;

import com.system.learn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserValidationService {

    @Autowired
    private MessageSource messageSource;
    private final UserRepository userRepository;

    @Autowired
    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserExists(String username, String email, Locale locale) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException(messageSource.getMessage("auth.user_by_nick_is_exists", null, locale));
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException(messageSource.getMessage("auth.user_by_email_is_exists", null, locale));
        }
    }

    public void validateUserByUsername(String username, Long currentUserId, Locale locale) {
        if (username != null) {
            userRepository.findByUsername(username)
                    .filter(user -> !user.getId().equals(currentUserId))
                    .ifPresent(user -> {
                        throw new RuntimeException(messageSource.getMessage("auth.user_by_nick_is_exists", null, locale));
                    });
        }
    }

    public void validateUserByEmail(String email, Long currentUserId, Locale locale) {

        if (email != null) {
            userRepository.findByEmail(email)
                    .filter(user -> !user.getId().equals(currentUserId))
                    .ifPresent(user -> {
                        throw new RuntimeException(messageSource.getMessage("auth.user_by_email_is_exists", null, locale));
                    });
        }
    }

    public void userIsNotExists(String username, Locale locale) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new RuntimeException(messageSource.getMessage("auth.user_by_nick_is_not_found", null, locale));
        }
    }


}