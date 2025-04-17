package com.system.learn.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserProfileDto {
    @Email(message = "Email не валидный")
    private String email;

    @Size(min = 3, max = 20, message = "Username должен быть от 3 до 20 символов")
    private String username;

    private Long learningLanguageId;

    public UserProfileDto(String username, String email, Long learningLanguageId) {
        this.username = username;
        this.email = email;
        this.learningLanguageId = learningLanguageId;
    }

    public UserProfileDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() { return email; }

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
