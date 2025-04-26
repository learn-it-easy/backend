package com.system.learn.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String mainWord;
    private boolean isImage;

    @Column(nullable = false)
    private String textTranslation;

    private LocalDateTime lastReviewedAt;
    private LocalDateTime nextReviewAt;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.HARD;

    private double repetitionCount = 0;
    private double easeFactor = 2.5;
    private double reviewInterval = 0;


    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private static final double EASE_FACTOR_DECREMENT = 0.15;
    private static final double EASE_FACTOR_INCREMENT = 0.10;
    private static final double MIN_EASE_FACTOR = 1.3;
    private static final double FIRST_INTERVAL = 1;
    private static final double SECOND_INTERVAL = 3;

    public String getMainWord() {
        return mainWord;
    }

    public void setMainWord(String mainWord) {
        this.mainWord = mainWord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }

    public String getTextTranslation() {
        return textTranslation;
    }

    public void setTextTranslation(String textTranslation) {
        this.textTranslation = textTranslation;
    }

    public LocalDateTime getLastReviewedAt() {
        return lastReviewedAt;
    }

    public void setLastReviewedAt(LocalDateTime lastReviewedAt) {
        this.lastReviewedAt = lastReviewedAt;
    }

    public LocalDateTime getNextReviewAt() {
        return nextReviewAt;
    }

    public void setNextReviewAt(LocalDateTime nextReviewAt) {
        this.nextReviewAt = nextReviewAt;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public double getEaseFactor() {
        return easeFactor;
    }

    public void setEaseFactor(double easeFactor) {
        this.easeFactor = easeFactor;
    }

    public double getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(double repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public double getReviewInterval() {
        return reviewInterval;
    }

    public void setReviewInterval(double reviewInterval) {
        this.reviewInterval = reviewInterval;
    }

    @Transactional
    public void processReview(Difficulty difficulty) {
        LocalDateTime now = LocalDateTime.now();
        this.lastReviewedAt = now;

        switch (difficulty) {
            case HARD:
                handleHardReview();
                break;
            case MEDIUM:
                handleMediumReview();
                break;
            case EASY:
                handleEasyReview();
                break;
        }

        // Дата следующего повторения
        this.nextReviewAt = now.plusDays((long) this.reviewInterval);
    }

    private void handleHardReview() {
        this.repetitionCount = 0;
        this.reviewInterval = FIRST_INTERVAL;
        this.easeFactor = Math.max(this.easeFactor - EASE_FACTOR_DECREMENT, MIN_EASE_FACTOR);
    }

    private void handleMediumReview() {
        if (this.repetitionCount == 0) {
            this.reviewInterval = FIRST_INTERVAL;
        } else if (this.repetitionCount == 1) {
            this.reviewInterval = SECOND_INTERVAL;
        } else {
            this.reviewInterval = (int) Math.round(this.reviewInterval * this.easeFactor);
        }

        this.repetitionCount += 1;
    }

    private void handleEasyReview() {
        if (this.repetitionCount == 0) {
            this.reviewInterval = FIRST_INTERVAL;
        } else if (this.repetitionCount == 1) {
            this.reviewInterval = SECOND_INTERVAL;
        } else {
            this.reviewInterval = (int) Math.round(this.reviewInterval * this.easeFactor);
        }

        this.repetitionCount += 1;
        this.easeFactor = Math.min(this.easeFactor + EASE_FACTOR_INCREMENT, 2.5);
    }



    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", user=" + user +
                ", folder=" + folder +
                ", text='" + text + '\'' +
                ", mainWord='" + mainWord + '\'' +
                ", isImage=" + isImage +
                ", textTranslation='" + textTranslation + '\'' +
                ", lastReviewedAt=" + lastReviewedAt +
                ", nextReviewAt=" + nextReviewAt +
                ", difficulty=" + difficulty +
                ", repetitionCount=" + repetitionCount +
                ", easeFactor=" + easeFactor +
                ", reviewInterval=" + reviewInterval +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return isImage == card.isImage && Double.compare(card.repetitionCount, repetitionCount) == 0 && Double.compare(card.easeFactor, easeFactor) == 0 && Double.compare(card.reviewInterval, reviewInterval) == 0 && Objects.equals(id, card.id) && Objects.equals(user, card.user) && Objects.equals(folder, card.folder) && Objects.equals(text, card.text) && Objects.equals(mainWord, card.mainWord) && Objects.equals(textTranslation, card.textTranslation) && Objects.equals(lastReviewedAt, card.lastReviewedAt) && Objects.equals(nextReviewAt, card.nextReviewAt) && difficulty == card.difficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, folder, text, mainWord, isImage, textTranslation, lastReviewedAt, nextReviewAt, difficulty, repetitionCount, easeFactor, reviewInterval);
    }
}