package com.system.learn.service;

import com.system.learn.entity.User;
import com.system.learn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserService {

    @Autowired
    private MessageSource messageSource;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUserById(Long userId, String lang){

        Locale locale = Locale.forLanguageTag(lang);

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("error.user_not_found", null, locale)
                ));
    }
}
