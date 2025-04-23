package com.system.learn.dto.card;

import java.time.LocalDateTime;

public class CardGetForFolderDto {

    private Long idCard;
    private String mainWord;
    private LocalDateTime nextReviewAt;

    public CardGetForFolderDto(Long idCard, String mainWord, LocalDateTime nextReviewAt) {
        this.idCard = idCard;
        this.mainWord = mainWord;
        this.nextReviewAt = nextReviewAt;
    }

    public CardGetForFolderDto() {}

    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public String getMainWord() {
        return mainWord;
    }

    public void setMainWord(String mainWord) { this.mainWord = mainWord; }

    public LocalDateTime getNextReviewAt() {
        return nextReviewAt;
    }

    public void setNextReviewAt(LocalDateTime nextReviewAt) {
        this.nextReviewAt = nextReviewAt;
    }
}
