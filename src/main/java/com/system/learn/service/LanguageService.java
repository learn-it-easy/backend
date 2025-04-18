package com.system.learn.service;

import com.system.learn.dto.LanguageDto;
import com.system.learn.entity.Language;
import com.system.learn.entity.User;
import com.system.learn.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LanguageService {
    @Autowired
    private MessageSource messageSource;
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }


    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public void setLearningLanguage(User user, Long learningLanguageId, Locale locale){
        user.setLearningLanguage(languageRepository.findById(learningLanguageId)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("error.not_such_language_in_learning", null, locale))));
    }

    public void setNativeLanguage(User user, Long nativeLanguageId, Locale locale){
        user.setNativeLanguage(languageRepository.findById(nativeLanguageId)
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("error.not_such_language_in_native", null, locale))));
    }

    private LanguageDto convertToDto(Language language) {
        return new LanguageDto(
                language.getId(),
                language.getName()
        );
    }
}