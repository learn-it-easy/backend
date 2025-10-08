package com.system.learn.controller;

import com.system.learn.dto.card.CardDto;
import com.system.learn.dto.card.CardPageDto;
import com.system.learn.dto.card.CardPartialUpdateDto;
import com.system.learn.service.CardService;
import com.system.learn.utils.CookieUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createCard(
                @RequestBody CardDto cardCreateDto,
                @RequestHeader("Authorization") String token,
                @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.createCardWithoutCheckIsImage(cardCreateDto, token, lang);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(
            @PathVariable Long cardId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.deleteCard(cardId, token, lang);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<?> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardPartialUpdateDto cardDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.updateCardWithoutCheckIsImage(cardId, cardDto, token, lang);
    }


    @GetMapping("/{cardId}")
    public CardDto getCard(
            @PathVariable Long cardId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.getCard(cardId, token, lang);
    }

    // получение следующей для повторения карточки из папки
    @GetMapping("/review/folders/{folderId}")
    public ResponseEntity<?> getNextCardForReviewFromFolder(
            @PathVariable Long folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.getNextCardForReviewFromFolder(folderId, token, lang);
    }

    // получение следующей для повторения карточки из всех папок
    @GetMapping("/review")
    public ResponseEntity<?> getNextCardForReviewAll(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.getNextCardForReviewAll(token, lang);
    }

    // получения карточек из всех папок
    @GetMapping("/")
    public CardPageDto getAllCards(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            @RequestParam(defaultValue = "1") int page) {

        return cardService.getAllCards(token, page, lang);
    }

    // получение всех карт из папки
    @GetMapping("/folders/{folderId}")
    public CardPageDto getAllCardsFromFolder(
            @PathVariable Long folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            @RequestParam(defaultValue = "1") int page) {

        return cardService.getAllCardsInFolder(folderId, token, page, lang);
    }



}
