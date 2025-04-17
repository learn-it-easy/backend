package com.system.learn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserProfileChangeDto {

    @Email(message = "Email не валидный")
    private String email;

    @Size(min = 3, max = 20, message = "Username должен быть от 3 до 20 символов")
    private String username;

    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    private String password;
    private Long learningLanguageId;

    public UserProfileChangeDto(String username, String password, String email, Long learningLanguageId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.learningLanguageId = learningLanguageId;
    }

    public UserProfileChangeDto() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLearningLanguageId() {
        return learningLanguageId;
    }

    public void setLearningLanguageId(Long learningLanguageId) {
        this.learningLanguageId = learningLanguageId;
    }

}
