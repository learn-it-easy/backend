package com.system.learn.dto.folder;

import com.system.learn.dto.card.ReviewTimeDto;

public class FolderGetDto {
    private Long id;
    private String name;
    private int cardCount;
    private ReviewTimeDto nearestReviewTime;

    public FolderGetDto(Long id, String name, int cardCount, ReviewTimeDto nearestReviewTime) {
        this.id = id;
        this.name = name;
        this.cardCount = cardCount;
        this.nearestReviewTime = nearestReviewTime;
    }

    public FolderGetDto() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
