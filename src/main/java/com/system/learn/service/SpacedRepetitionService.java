package com.system.learn.service;

import com.system.learn.dto.ErrorResponseDto;
import com.system.learn.entity.Card;
import com.system.learn.entity.User;
import com.system.learn.repository.CardRepository;
import com.system.learn.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class SpacedRepetitionService {


    @Autowired
    private MessageSource messageSource;
    private final CardRepository cardRepository;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public SpacedRepetitionService(CardRepository cardRepository, JwtUtils jwtUtils, UserService userService) {
        this.cardRepository = cardRepository;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Transactional
    public ResponseEntity<?> processCardReview(Long cardId, String difficulty, String token, String lang) {
        try {
            Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
            User currentUser = userService.getUserById(currentUserId, lang);

            Card card = cardRepository.findByIdAndUser(cardId, currentUser)
                    .orElseThrow(() -> new EntityNotFoundException(
                            messageSource.getMessage("card.not_found", null, Locale.forLanguageTag(lang))
                    ));

            Card.Difficulty difficultyEnum = Card.Difficulty.valueOf(difficulty.toUpperCase());

            card.processReview(difficultyEnum);
            cardRepository.save(card);

            return ResponseEntity.ok(new ErrorResponseDto(
                    null,
                    messageSource.getMessage("card.review_processed", null, Locale.forLanguageTag(lang))
            ));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("cardId", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(
                    "difficulty",
                    messageSource.getMessage("card.invalid_difficulty", null, Locale.forLanguageTag(lang))
            ));
        }
    }
}