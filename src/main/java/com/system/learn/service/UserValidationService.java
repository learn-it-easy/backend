package com.system.learn.service;

import com.system.learn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final UserRepository userRepository;

    @Autowired
    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserExists(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким username уже существует");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Пользователь с такой почтой уже существует");
        }
    }

    public void validateUserByUsername(String username, Long currentUserId) {
        if (username != null) {
            userRepository.findByUsername(username)
                    .filter(user -> !user.getId().equals(currentUserId))
                    .ifPresent(user -> {
                        throw new RuntimeException("Пользователь с таким username уже существует");
                    });
        }
    }

    public void validateUserByEmail(String email, Long currentUserId) {

        if (email != null) {
            userRepository.findByEmail(email)
                    .filter(user -> !user.getId().equals(currentUserId))
                    .ifPresent(user -> {
                        throw new RuntimeException("Пользователь с такой почтой уже существует");
                    });
        }
    }

    public void userIsNotExists(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new RuntimeException("Пользователь с таким username не найден");
        }
    }


}