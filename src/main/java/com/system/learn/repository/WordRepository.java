package com.system.learn.repository;

import com.system.learn.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface WordRepository extends JpaRepository<Word, Long> {


    // Получение слов для повторения
    @Query("SELECT w FROM Word w WHERE w.user.id = :userId AND " +
            "(w.nextReviewAt IS NULL OR w.nextReviewAt <= :now)")
    List<Word> findWordsForReview(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );

}