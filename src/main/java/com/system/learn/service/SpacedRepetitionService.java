package com.system.learn.service;

import com.system.learn.entity.Word;
import com.system.learn.entity.Word.Difficulty;
import com.system.learn.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SpacedRepetitionService {

    private final WordRepository wordRepository;

    public SpacedRepetitionService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Transactional
    public void processReview(Word word, Difficulty difficulty) {
        // Обновляем базовые поля
        word.setLastReviewedAt(LocalDateTime.now());
        word.setDifficulty(difficulty);
        word.setRepetitionCount(word.getRepetitionCount() + 1);

        // Вычисляем новые значения
        int newInterval = calculateReviewInterval(word, difficulty);
        double newEaseFactor = calculateEaseFactor(word.getEaseFactor(), difficulty);

        // Применяем изменения
        word.setReviewInterval(newInterval);
        word.setEaseFactor(newEaseFactor);
        word.setNextReviewAt(
                word.getLastReviewedAt().plusDays(newInterval)
        );

        wordRepository.save(word);
    }

    private int calculateReviewInterval(Word word, Difficulty difficulty) {
        if (word.getRepetitionCount() == 1) {
            return switch (difficulty) {
                case EASY -> 4;
                case MEDIUM -> 2;
                case HARD -> 1;
            };
        }

        return (int) (word.getReviewInterval() * switch (difficulty) {
            case EASY -> word.getEaseFactor() * 1.3;
            case MEDIUM -> word.getEaseFactor();
            case HARD -> word.getEaseFactor() * 0.8;
        });
    }

    private double calculateEaseFactor(double currentEase, Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> Math.min(currentEase + 0.15, 2.5);
            case HARD -> Math.max(currentEase - 0.15, 1.3);
            case MEDIUM -> currentEase;
        };
    }
}