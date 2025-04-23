package com.system.learn.controller;


import com.system.learn.service.SpacedRepetitionService;
import com.system.learn.utils.CookieUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/space-repetition")
public class SpacedRepetitionController {

   private final SpacedRepetitionService spacedRepetitionService;

    public SpacedRepetitionController(SpacedRepetitionService spacedRepetitionService) {
        this.spacedRepetitionService = spacedRepetitionService;
    }


    @PostMapping
    public ResponseEntity<?> processCardReview(
            @RequestParam int cardId,
            @RequestParam String difficulty, // "easy", "medium", "hard"
            @RequestHeader("Authorization") String token,
            @CookieValue(value = CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = "ru") String lang) {

        return spacedRepetitionService.processCardReview(Long.valueOf(cardId), difficulty, token, lang);
    }
}
