package com.system.learn.service;

import com.system.learn.dto.card.*;
import com.system.learn.dto.ErrorResponseDto;
import com.system.learn.entity.Card;
import com.system.learn.entity.Folder;
import com.system.learn.entity.User;
import com.system.learn.repository.CardRepository;
import com.system.learn.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private MessageSource messageSource;
    private final FolderService folderService;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    public CardService(FolderService folderService, CardRepository cardRepository, UserService userService, JwtUtils jwtUtils) {
        this.folderService = folderService;
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }


    @Value("${app.cards-return-count}")
    private int countOfReturningCards;

    public ResponseEntity<?> createCard(
            CardDto cardCreateDto,
            String token,
            String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User user = userService.getUserById(currentUserId, lang);
        Card card = new Card();

        if (cardCreateDto.getFolderId() != null) {
            Folder folder = folderService.getFolderByUserAndFolderId(user, cardCreateDto.getFolderId(), lang);
            card.setFolder(folder);
        } else {
            card.setFolder(null);
        }

        if (cardCreateDto.getIsImage()) {
            if (!isValidImageLink(cardCreateDto.getTextTranslation())) {
                return ResponseEntity.badRequest().body(new ErrorResponseDto(
                        "textTranslation",
                        messageSource.getMessage("card.invalid_image_link", null, Locale.forLanguageTag(lang))
                ));
            }
            if (oneWordInSentence(cardCreateDto.getText())) {
                cardCreateDto.setText(ensureHighlightedWords(cardCreateDto.getText()));

            } else if (!hasHighlightedWords(cardCreateDto.getText())) {
                return ResponseEntity.badRequest().body(new ErrorResponseDto(
                        "text",
                        messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                ));
            }
        } else {
            if (oneWordInSentence(cardCreateDto.getText())) {
                cardCreateDto.setText(ensureHighlightedWords(cardCreateDto.getText()));

            }
            if (oneWordInSentence(cardCreateDto.getTextTranslation())) {
                cardCreateDto.setTextTranslation(ensureHighlightedWords(cardCreateDto.getTextTranslation()));

            }
            if (!hasHighlightedWords(cardCreateDto.getText()) || !hasHighlightedWords(cardCreateDto.getTextTranslation())) {
                return ResponseEntity.badRequest().body(new ErrorResponseDto(
                        "text",
                        messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                ));
            }
        }

        card.setUser(user);
        card.setText(cardCreateDto.getText());
        card.setTextTranslation(cardCreateDto.getTextTranslation());
        card.setMainWord(extractHighlightedWords(cardCreateDto.getText()));
        card.setIsImage(cardCreateDto.getIsImage());
        card.setLastReviewedAt(null);
        card.setNextReviewAt(LocalDateTime.now());

        cardRepository.save(card);

        return ResponseEntity.ok(new ErrorResponseDto(null,
                messageSource.getMessage("card.created_success",
                        null,
                        Locale.forLanguageTag(lang))));
    }


    public ResponseEntity<?> createCardWithoutCheckIsImage(
            CardDto cardCreateDto,
            String token,
            String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User user = userService.getUserById(currentUserId, lang);
        Card card = new Card();

        if (cardCreateDto.getFolderId() != null) {
            Folder folder = folderService.getFolderByUserAndFolderId(user, cardCreateDto.getFolderId(), lang);
            card.setFolder(folder);
        } else {
            card.setFolder(null);
        }

        if (cardCreateDto.getIsImage()) {
            if (oneWordInSentence(cardCreateDto.getText())) {
                cardCreateDto.setText(ensureHighlightedWords(cardCreateDto.getText()));

            } else if (!hasHighlightedWords(cardCreateDto.getText())) {
                return ResponseEntity.badRequest().body(new ErrorResponseDto(
                        "text",
                        messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                ));
            }
        } else {
            if (oneWordInSentence(cardCreateDto.getText())) {
                cardCreateDto.setText(ensureHighlightedWords(cardCreateDto.getText()));

            }
            if (oneWordInSentence(cardCreateDto.getTextTranslation())) {
                cardCreateDto.setTextTranslation(ensureHighlightedWords(cardCreateDto.getTextTranslation()));

            }
            if (!hasHighlightedWords(cardCreateDto.getText()) || !hasHighlightedWords(cardCreateDto.getTextTranslation())) {
                return ResponseEntity.badRequest().body(new ErrorResponseDto(
                        "text",
                        messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                ));
            }
        }

        card.setUser(user);
        card.setText(cardCreateDto.getText());
        card.setTextTranslation(cardCreateDto.getTextTranslation());
        card.setMainWord(extractHighlightedWords(cardCreateDto.getText()));
        card.setIsImage(cardCreateDto.getIsImage());
        card.setLastReviewedAt(null);
        card.setNextReviewAt(LocalDateTime.now());

        cardRepository.save(card);

        return ResponseEntity.ok(new ErrorResponseDto(null,
                messageSource.getMessage("card.created_success",
                        null,
                        Locale.forLanguageTag(lang))));
    }

    @Transactional
    public ResponseEntity<?> deleteCard(
            Long cardId,
            String token,
            String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User user = userService.getUserById(currentUserId, lang);

        int deletedCount = cardRepository.deleteByIdAndUser(cardId, user);

        if (deletedCount == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto(
                            "cardId",
                            messageSource.getMessage("card.not_found", null, Locale.forLanguageTag(lang))
                    ));
        }

        return ResponseEntity.ok(new ErrorResponseDto(
                null,
                messageSource.getMessage("card.delete_success", null, Locale.forLanguageTag(lang))
        ));
    }

    @Transactional
    public ResponseEntity<?> updateCard(Long cardId, CardPartialUpdateDto dto, String token, String lang) {

        try {
            Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
            User currentUser = userService.getUserById(currentUserId, lang);
            Card card = getCardByIdAndUser(cardId, currentUser, lang);

            if (dto.getText() != null && !dto.getText().isEmpty()) {
                    if (oneWordInSentence(dto.getText())) {
                        dto.setText(ensureHighlightedWords(dto.getText()));
                    } else if (!hasHighlightedWords(dto.getText())) {
                        return ResponseEntity.badRequest().body(new ErrorResponseDto(
                                "text",
                                messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                        ));
                    }

                card.setText(dto.getText());
                card.setMainWord(extractHighlightedWords(dto.getText()));
            }

            if (dto.getTextTranslation() != null && !dto.getTextTranslation().isEmpty()) {
                if (dto.getIsImage() || (!dto.getIsImage() && card.getIsImage())) {
                    if (!isValidImageLink(dto.getTextTranslation())) {
                        return ResponseEntity.badRequest().body(new ErrorResponseDto(
                                "textTranslation",
                                messageSource.getMessage("card.invalid_image_link", null, Locale.forLanguageTag(lang))
                        ));
                    }
                } else {
                    if (oneWordInSentence(dto.getTextTranslation())) {
                        dto.setTextTranslation(ensureHighlightedWords(dto.getTextTranslation()));
                    }
                    if (!hasHighlightedWords(dto.getTextTranslation())) {
                        return ResponseEntity.badRequest().body(new ErrorResponseDto(
                                "text",
                                messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                        ));
                    }
                }
                card.setTextTranslation(dto.getTextTranslation());
            }

            if (dto.getFolderId() != null || (dto.getFolderId() == null && card.getFolder() != null)) {
                if (dto.getFolderId() == null) {
                    card.setFolder(null);
                } else {
                    Folder folder = folderService.getFolderByUserAndFolderId(currentUser, dto.getFolderId(), lang);
                    card.setFolder(folder);
                }
            }

            if (dto.getIsImage() != null) {
                card.setIsImage(dto.getIsImage());
            }

            cardRepository.save(card);

            return ResponseEntity.ok(new ErrorResponseDto(
                    null,
                    messageSource.getMessage("card.update_success", null, Locale.forLanguageTag(lang))
            ));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("cardId", e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateCardWithoutCheckIsImage(Long cardId, CardPartialUpdateDto dto, String token, String lang) {

        try {
            Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
            User currentUser = userService.getUserById(currentUserId, lang);
            Card card = getCardByIdAndUser(cardId, currentUser, lang);

            if (dto.getText() != null && !dto.getText().isEmpty()) {
                if (oneWordInSentence(dto.getText())) {
                    dto.setText(ensureHighlightedWords(dto.getText()));
                } else if (!hasHighlightedWords(dto.getText())) {
                    return ResponseEntity.badRequest().body(new ErrorResponseDto(
                            "text",
                            messageSource.getMessage("card.missing_highlighted_words", null, Locale.forLanguageTag(lang))
                    ));
                }

                card.setText(dto.getText());
                card.setMainWord(extractHighlightedWords(dto.getText()));
            }


            card.setTextTranslation(dto.getTextTranslation());

            if (dto.getFolderId() != null || (dto.getFolderId() == null && card.getFolder() != null)) {
                if (dto.getFolderId() == null) {
                    card.setFolder(null);
                } else {
                    Folder folder = folderService.getFolderByUserAndFolderId(currentUser, dto.getFolderId(), lang);
                    card.setFolder(folder);
                }
            }

            if (dto.getIsImage() != null) {
                card.setIsImage(dto.getIsImage());
            }

            cardRepository.save(card);

            return ResponseEntity.ok(new ErrorResponseDto(
                    null,
                    messageSource.getMessage("card.update_success", null, Locale.forLanguageTag(lang))
            ));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("cardId", e.getMessage()));
        }
    }



    public CardPageDto getAllCardsInFolder(Long folderId, String token, int page, String lang) {

        int defaultPageSize = countOfReturningCards;
        int firstPage = 1;

        if (page < firstPage) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "cards.out_of_bounds_less",
                    new Object[]{firstPage},
                    Locale.forLanguageTag(lang)));
        }

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User currentUser = userService.getUserById(currentUserId, lang);

        Folder folder = folderService.getFolderByUserAndFolderId(currentUser, folderId, lang);


        long totalCards = cardRepository.countByFolderAndUser(folder, currentUser);

        int totalPages = (int) Math.ceil((double) totalCards / defaultPageSize);

        if (page > totalPages && totalPages > 0) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "cards.out_of_bounds_more",
                    new Object[]{firstPage, totalPages},
                    Locale.forLanguageTag(lang)));
        }

        int offset = (page - 1) * defaultPageSize;
        int limit = defaultPageSize;

        List<Card> cards = cardRepository.findByFolderAndUserWithPagination(folderId, currentUserId, offset, limit);

        List<CardGetForFolderDto> cardDtos = cards.stream()
                .map(card -> new CardGetForFolderDto(
                        card.getId(),
                        card.getMainWord(),
                        card.getNextReviewAt()
                ))
                .collect(Collectors.toList());

        // Возвращаем объект пагинации
        return new CardPageDto(
                cardDtos,
                page,
                totalPages,
                page < totalPages, // hasNext
                page > 1          // hasPrevious
        );

    }


    public CardPageDto getAllCards(String token, int page, String lang) {

        int defaultPageSize = countOfReturningCards;
        int firstPage = 1;

        if (page < firstPage) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "cards.out_of_bounds_less",
                    new Object[]{firstPage},
                    Locale.forLanguageTag(lang)));
        }

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User user = userService.getUserById(currentUserId, lang);


        long totalCards = cardRepository.countByUser(user);

        int totalPages = (int) Math.ceil((double) totalCards / defaultPageSize);

        if (page > totalPages && totalPages > 0) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "cards.out_of_bounds_more",
                    new Object[]{firstPage, totalPages},
                    Locale.forLanguageTag(lang)));
        }

        int offset = (page - 1) * defaultPageSize;
        int limit = defaultPageSize;

        List<Card> cards = cardRepository.findByUserWithOffsetAndLimit(user.getId(), offset, limit);

        List<CardGetForFolderDto> cardDtos = cards.stream()
                .map(card -> new CardGetForFolderDto(
                        card.getId(),
                        card.getMainWord(),
                        card.getNextReviewAt()
                ))
                .collect(Collectors.toList());

        return new CardPageDto(
                cardDtos,
                page,
                totalPages,
                page < totalPages, // hasNext
                page > 1          // hasPrevious
        );
    }

    // Вспомогательные функции

    private boolean hasHighlightedWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("==\\s*([^=]+?)\\s*==");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            if (!matcher.group(1).trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private String extractHighlightedWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        List<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile("==\\s*([^=]+?)\\s*==");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String word = matcher.group(1).trim();
            if (!word.isEmpty()) {
                String capitalized = word.substring(0, 1).toUpperCase()
                        + word.substring(1).toLowerCase();
                words.add(capitalized);
            }
        }

        return String.join(", ", words);
    }

    private boolean isValidImageLink(String link) {

        try {
            new URL(link); // Валидность URL

            // Проверка расширений для картинки
            String lowerLink = link.toLowerCase();
            return lowerLink.matches(".*\\.(jpg|jpeg|png|gif|webp|bmp)(\\?.*)?$");

        } catch (MalformedURLException e) {
            return false;
        }
    }

    private String ensureHighlightedWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        if (hasHighlightedWords(text)) {
            return text;
        }

        if (oneWordInSentence(text)) {
            return "==" + text + "==";
        }

        return text;
    }

    private boolean oneWordInSentence(String text) {
        String trimmed = text.trim();
        if (trimmed.split("\\s+").length == 1) {
            return true;
        }
        return false;
    }

    public CardDto getCard(Long cardId, String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User currentUser = userService.getUserById(currentUserId, lang);

        Card card = getCardByIdAndUser(cardId, currentUser, lang);

        CardDto dto = new CardDto();

        dto.setFolderId(card.getFolder() != null ? card.getFolder().getId() : null);
        dto.setIsImage(card.getIsImage());
        dto.setText(card.getText());
        dto.setTextTranslation(card.getTextTranslation());
        dto.setCardId(cardId);

        return dto;
    }

    public Card getCardByIdAndUser(Long cardId, User currentUser, String lang) {
        return cardRepository.findByIdAndUser(cardId, currentUser)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("card.not_found", null, Locale.forLanguageTag(lang))
                ));
    }

    public ResponseEntity<?> getNextCardForReviewAll(String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));

        Optional<CardDtoProjection> cardProjection = cardRepository.findNextCardForReview(
                currentUserId,
                LocalDateTime.now()
        );

        if (cardProjection.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponseDto(
                    null,
                    messageSource.getMessage("card.no_cards_for_review", null, Locale.forLanguageTag(lang))
            ));
        }

        CardDtoProjection proj = cardProjection.get();
        CardDto cardDto = new CardDto(
                proj.getFolderId(),
                proj.getText(),
                proj.getTextTranslation(),
                proj.getIsImage(),
                proj.getCardId()
        );

        return ResponseEntity.ok(cardDto);
    }


    public ResponseEntity<?> getNextCardForReviewFromFolder(Long folderId, String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));

        Optional<CardDtoProjection> cardProjection = cardRepository.findNextCardForReviewFromFolder(
                currentUserId,
                folderId,
                LocalDateTime.now()
        );

        if (cardProjection.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponseDto(
                    null,
                    messageSource.getMessage("card.no_cards_for_review_in_folder", null, Locale.forLanguageTag(lang))
            ));
        }

        CardDtoProjection proj = cardProjection.get();
        CardDto cardDto = new CardDto(
                proj.getFolderId(),
                proj.getText(),
                proj.getTextTranslation(),
                proj.getIsImage(),
                proj.getCardId()
        );

        return ResponseEntity.ok(cardDto);

    }

}
