package com.system.learn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {
    @NotBlank(message = "Заполните поле email")
    @Email(message = "Email не валидный")
    private String email;

    @NotBlank(message = "Заполните поле username")
    @Size(min = 3, max = 20, message = "Username должен быть от 3 до 20 символов")
    private String username;

    @NotBlank(message = "Заполните поле пароль")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String password;

    @NotNull(message = "Заполните изучаемого языка")
    private Long learningLanguageId;

    @NotNull(message = "Заполните Вашего родного языка")
    private Long nativeLanguageId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getLearningLanguageId() {
        return learningLanguageId;
    }

    public void setLearningLanguageId(Long learningLanguageId) {
        this.learningLanguageId = learningLanguageId;
    }

    public Long getNativeLanguageId() {
        return nativeLanguageId;
    }

    public void setNativeLanguageId(Long nativeLanguageId) {
        this.nativeLanguageId = nativeLanguageId;
    }
}