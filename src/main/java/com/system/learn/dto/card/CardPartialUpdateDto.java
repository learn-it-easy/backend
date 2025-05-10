package com.system.learn.dto.card;

import jakarta.annotation.Nullable;

public class CardPartialUpdateDto {

    @Nullable
    private Long folderId;

    @Nullable
    private String text;

    @Nullable
    private String textTranslation;

    @Nullable
    private Boolean isImage;


    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextTranslation() {
        return textTranslation;
    }

    public void setTextTranslation(String textTranslation) {
        this.textTranslation = textTranslation;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(Boolean image) {
        isImage = image;
    }
}
