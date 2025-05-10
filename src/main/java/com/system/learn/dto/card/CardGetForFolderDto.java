package com.system.learn.dto.card;

import java.time.LocalDateTime;

public class CardGetForFolderDto {

    private Long cardId;
    private String mainWord;
    private LocalDateTime nextReviewAt;

    public CardGetForFolderDto(Long idCard, String mainWord, LocalDateTime nextReviewAt) {
        this.cardId = idCard;
        this.mainWord = mainWord;
        this.nextReviewAt = nextReviewAt;
    }

    public CardGetForFolderDto() {}

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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
