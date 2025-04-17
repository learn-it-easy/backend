package com.system.learn.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "words")
public class Word {
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
    private String word;

    private boolean isImage;

    @Column(nullable = false)
    private String translation;

    private LocalDateTime lastReviewedAt;
    private LocalDateTime nextReviewAt;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty = Difficulty.HARD;

    private int repetitionCount = 0;
    private double easeFactor = 2.5;
    private int reviewInterval = 0;


    public enum Difficulty {
        EASY, MEDIUM, HARD
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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

    public int getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(int repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public double getEaseFactor() {
        return easeFactor;
    }

    public void setEaseFactor(double easeFactor) {
        this.easeFactor = easeFactor;
    }

    public int getReviewInterval() {
        return reviewInterval;
    }

    public void setReviewInterval(int reviewInterval) {
        this.reviewInterval = reviewInterval;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", user=" + user +
                ", folder=" + folder +
                ", word='" + word + '\'' +
                ", isImage=" + isImage +
                ", translation='" + translation + '\'' +
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
        if (!(o instanceof Word word1)) return false;
        return isImage == word1.isImage && repetitionCount == word1.repetitionCount && Double.compare(word1.easeFactor, easeFactor) == 0 && reviewInterval == word1.reviewInterval && Objects.equals(id, word1.id) && Objects.equals(user, word1.user) && Objects.equals(folder, word1.folder) && Objects.equals(word, word1.word) && Objects.equals(translation, word1.translation) && Objects.equals(lastReviewedAt, word1.lastReviewedAt) && Objects.equals(nextReviewAt, word1.nextReviewAt) && difficulty == word1.difficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, folder, word, isImage, translation, lastReviewedAt, nextReviewAt, difficulty, repetitionCount, easeFactor, reviewInterval);
    }
}