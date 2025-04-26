package com.system.learn.dto.folder;

import com.system.learn.dto.card.ReviewTimeDto;

public class AllCardsDto {
    private int cardCount;
    private ReviewTimeDto nearestReviewTime;

    public AllCardsDto(int cardCount, ReviewTimeDto nearestReviewTime) {
        this.cardCount = cardCount;
        this.nearestReviewTime = nearestReviewTime;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public ReviewTimeDto getNearestReviewTime() {
        return nearestReviewTime;
    }

    public void setNearestReviewTime(ReviewTimeDto nearestReviewTime) {
        this.nearestReviewTime = nearestReviewTime;
    }
}