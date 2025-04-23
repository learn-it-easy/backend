package com.system.learn.dto.profile;

public class UserProfileChangeDto {

    private String email;
    private String username;
    private String password;
    private Long learningLanguageId;
    private Long nativeLanguageId;

    public UserProfileChangeDto(String username, String password, String email, Long learningLanguageId, Long nativeLanguageId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.learningLanguageId = learningLanguageId;
        this.nativeLanguageId = nativeLanguageId;
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

    public Long getNativeLanguageId() {
        return nativeLanguageId;
    }

    public void setNativeLanguageId(Long nativeLanguageId) {
        this.nativeLanguageId = nativeLanguageId;
    }
}
