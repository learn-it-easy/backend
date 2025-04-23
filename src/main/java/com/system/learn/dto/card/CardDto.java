package com.system.learn.dto.card;

public class CardDto {
    private Long folderId;
    private String text;
    private String textTranslation;
    private boolean isImage;
    private Long cardId;

    public CardDto(Long folderId, String text, String textTranslation, boolean isImage) {
        this.folderId = folderId;
        this.text = text;
        this.textTranslation = textTranslation;
        this.isImage = isImage;
    }

    public CardDto(Long folderId, String text, String textTranslation, boolean isImage, Long cardId) {
        this.folderId = folderId;
        this.text = text;
        this.textTranslation = textTranslation;
        this.isImage = isImage;
        this.cardId = cardId;
    }

    public CardDto() {
    }

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

    public boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
