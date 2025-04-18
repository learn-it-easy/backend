package com.system.learn.dto;

public class UserRegistrationDto {

    private String email;
    private String username;
    private String password;
    private Long learningLanguageId;
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