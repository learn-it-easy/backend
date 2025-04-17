package com.system.learn.service;

import com.system.learn.dto.AuthResponseDto;
import com.system.learn.dto.LoginRequestDto;
import com.system.learn.dto.UserRegistrationDto;
import com.system.learn.entity.User;
import com.system.learn.repository.LanguageRepository;
import com.system.learn.repository.UserRepository;
import com.system.learn.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final UserValidationService userValidationService;

    public UserAuthService(UserRepository userRepository,
                       LanguageRepository languageRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       UserValidationService userValidationService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userValidationService = userValidationService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponseDto registerUser(UserRegistrationDto registrationDto) {

        userValidationService.validateUserExists(registrationDto.getUsername(), registrationDto.getEmail());

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setLearningLanguage(languageRepository.findById(registrationDto.getLearningLanguageId())
                .orElseThrow(() -> new RuntimeException("Нет такого языка в поле изучаемый")));
        user.setNativeLanguage(languageRepository.findById(registrationDto.getNativeLanguageId())
                .orElseThrow(() -> new RuntimeException("Нет такого языка в поле родной")));

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser.getUsername(), savedUser.getId());
        return new AuthResponseDto(token);
    }



    public AuthResponseDto authenticateUser(LoginRequestDto loginRequestDto) {

        userValidationService.userIsNotExists(loginRequestDto.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());

            return new AuthResponseDto(token);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Неверный пароль");
        }
    }

    public AuthResponseDto getToken(Long userId, String username){
        return new AuthResponseDto(
                jwtTokenProvider.generateToken(username,userId)
        );
    }

    public AuthResponseDto getToken(Long userId, String username, String password) {
        if (password == null) {
            // Если пароль не менялся — находим пользователя по username и генерируем токен
            return getToken(userId, username);
        } else {
            // Если пароль менялся — стандартная аутентификация
            return authenticateUser(new LoginRequestDto(username, password));
        }
    }

}
