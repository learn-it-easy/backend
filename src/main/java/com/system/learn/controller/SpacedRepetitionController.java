package com.system.learn.controller;


import com.system.learn.entity.Word;
import com.system.learn.repository.WordRepository;
import com.system.learn.service.SpacedRepetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/space-repetition")
public class SpacedRepetitionController {
    private final SpacedRepetitionService repetitionService;
    private final WordRepository wordRepository;

    public SpacedRepetitionController(SpacedRepetitionService repetitionService, WordRepository wordRepository) {
        this.repetitionService = repetitionService;
        this.wordRepository = wordRepository;
    }

    @PostMapping("/{wordId}/review")
    public ResponseEntity<Void> processReview(
            @PathVariable Long wordId,
            @RequestParam Word.Difficulty difficulty
    ) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        repetitionService.processReview(word, difficulty);
        return ResponseEntity.ok().build();
    }
}
