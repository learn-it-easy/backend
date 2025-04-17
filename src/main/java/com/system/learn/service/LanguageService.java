package com.system.learn.service;

import com.system.learn.dto.LanguageDto;
import com.system.learn.entity.Language;
import com.system.learn.entity.User;
import com.system.learn.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

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

    public void setLanguage(User user, Long languageId){
        user.setLearningLanguage(languageRepository.findById(languageId)
                .orElseThrow(() -> new RuntimeException("Ошибка при выборе языка, такого нет")));
    }

    private LanguageDto convertToDto(Language language) {
        return new LanguageDto(
                language.getId(),
                language.getName()
        );
    }
}