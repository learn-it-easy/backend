package com.system.learn.repository;

import com.system.learn.dto.card.CardDto;
import com.system.learn.dto.card.CardDtoProjection;
import com.system.learn.entity.Card;
import com.system.learn.entity.Folder;
import com.system.learn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {


    // Получение слов для повторения
    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND " +
            "(c.nextReviewAt IS NULL OR c.nextReviewAt <= :now)")
    List<Card> findCardsForReview(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Card c WHERE c.id = :cardId AND c.user = :user")
    int deleteByIdAndUser(@Param("cardId") Long cardId, @Param("user") User user);

    @Query(value = "SELECT * FROM cards WHERE folder_id = :folder_id AND user_id = :user_id ORDER BY id ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Card> findByFolderAndUserWithPagination(
            @Param("folder_id") Long folderId,
            @Param("user_id") Long userId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    long countByFolderAndUser(Folder folder, User user);

    @Query(value = "SELECT * FROM cards WHERE user_id = :userId ORDER BY id ASC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Card> findByUserWithOffsetAndLimit(
            @Param("userId") Long userId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    long countByUser(User user);
    Optional<Card> findByIdAndUser(Long cardId, User currentUser);

    List<Card> findByFolderAndUser(Folder folder, User user);


    @Query(value = """
        SELECT 
            c.id as cardId,
            c.folder_id as folderId,
            c.text as text,
            c.text_translation as textTranslation,
            c.is_image as isImage
        FROM cards c
        WHERE c.user_id = :userId
        AND c.next_review_at <= :currentTime
        ORDER BY c.next_review_at ASC
        LIMIT 1
        """, nativeQuery = true)
    Optional<CardDtoProjection> findNextCardForReview(
            @Param("userId") Long userId,
            @Param("currentTime") LocalDateTime currentTime);

    @Query(value = """
        SELECT 
            c.id as cardId,
            c.folder_id as folderId,
            c.text as text,
            c.text_translation as textTranslation,
            c.is_image as isImage
        FROM cards c
        WHERE c.user_id = :userId
        AND c.folder_id = :folderId
        AND c.next_review_at <= :currentTime
        ORDER BY c.next_review_at ASC
        LIMIT 1
        """, nativeQuery = true)
    Optional<CardDtoProjection> findNextCardForReviewFromFolder(
            @Param("userId") Long userId,
            @Param("folderId") Long folderId,
            @Param("currentTime") LocalDateTime currentTime);
}