package com.system.learn.controller;

import com.system.learn.dto.card.CardDto;
import com.system.learn.dto.card.CardPageDto;
import com.system.learn.dto.card.CardPartialUpdateDto;
import com.system.learn.service.CardService;
import com.system.learn.utils.CookieUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCard(
                @RequestBody CardDto cardCreateDto,
                @RequestHeader("Authorization") String token,
                @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.createCard(cardCreateDto, token, lang);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCard(
            @RequestParam int cardId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.deleteCard(Long.valueOf(cardId), token, lang);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateCard(
            @RequestBody CardPartialUpdateDto cardDto,
            @RequestParam int cardId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.updateCard(Long.valueOf(cardId), cardDto, token, lang);
    }


    @PostMapping("/get")
    public CardDto getCard(
            @RequestParam Long cardId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.getCard(cardId, token, lang);
    }

    @GetMapping("/get/review-folder")
    public ResponseEntity<?> getNextCardForReviewFromFolder(
            @RequestParam int folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.getNextCardForReviewFromFolder(Long.valueOf(folderId), token, lang);
    }

    @GetMapping("/get/review-all")
    public ResponseEntity<?> getNextCardForReviewAll(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return cardService.getNextCardForReviewAll(token, lang);
    }


    @GetMapping("/get/all")
    public CardPageDto getAllCards(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            @RequestParam(defaultValue = "1") int page) {

        return cardService.getAllCards(token, page, lang);
    }


    @PostMapping("/from-folder")
    public CardPageDto getAllCardsFromFolder(
            @RequestParam int folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang,
            @RequestParam(defaultValue = "1") int page) {

        return cardService.getAllCardsInFolder(Long.valueOf(folderId), token, page, lang);
    }



}
