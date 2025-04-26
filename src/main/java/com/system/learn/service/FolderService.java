package com.system.learn.service;

import com.system.learn.dto.ErrorResponseDto;
import com.system.learn.dto.card.ReviewTimeDto;
import com.system.learn.dto.folder.AllCardsDto;
import com.system.learn.dto.folder.FolderCreateDto;
import com.system.learn.dto.folder.FolderGetDto;
import com.system.learn.dto.folder.FolderPageDto;
import com.system.learn.entity.Card;
import com.system.learn.entity.Folder;
import com.system.learn.entity.User;
import com.system.learn.repository.CardRepository;
import com.system.learn.repository.FolderRepository;
import com.system.learn.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Autowired
    private MessageSource messageSource;
    private final UserService userService;
    private final FolderRepository folderRepository;
    private final JwtUtils jwtUtils;
    private final CardRepository cardRepository;
    private final ReviewTimeService reviewTimeService;


    public FolderService(UserService userService, FolderRepository folderRepository, JwtUtils jwtUtils, CardRepository cardRepository, ReviewTimeService reviewTimeService) {
        this.userService = userService;
        this.folderRepository = folderRepository;
        this.jwtUtils = jwtUtils;
        this.cardRepository = cardRepository;
        this.reviewTimeService = reviewTimeService;
    }

    @Value("${app.folder-return-count}")
    private int countOfReturningFolders;
    public Folder createFolder(FolderCreateDto folderCreateDto, String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));

       User user = userService.getUserById(currentUserId, lang);

        Folder folder = new Folder();
        folder.setName(folderCreateDto.getName());
        folder.setUser(user);
        folderRepository.save(folder);
        return folder;
    }

    public FolderPageDto getUserFolders(String token, int page, String lang) {

        int firstPage = 1;
        int defaultPageSize = countOfReturningFolders;

        // Размер первой страницы (на 1 меньше), чтобы на фронтенде добавить первой папкой
        // "Все", не изменяя кол-во папок на странице - которая по сути не папка, а просто вывод всех карточек
        // пользователя и из других папок, и без папок впринципе
        int firstPageSize = defaultPageSize - 1;

        if (page < firstPage) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "folders.out_of_bounds_less",
                    new Object[]{firstPage},
                    Locale.forLanguageTag(lang)));
        }

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
        User user = userService.getUserById(currentUserId, lang);

        long totalFolders = folderRepository.countByUser(user);

        // Расчёт страниц
        int totalPages;
        if (totalFolders <= firstPageSize) {
            totalPages = 1;
        } else {
            totalPages = 1 + (int) Math.ceil((double)(totalFolders - firstPageSize) / defaultPageSize);
        }

        if (page > totalPages && totalPages > 0) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "folders.out_of_bounds_more",
                    new Object[]{firstPage, totalPages},
                    Locale.forLanguageTag(lang)));
        }

        // Праметры для запроса
        int offset;
        int limit;

        if (page == 1) {
            offset = 0;
            limit = firstPageSize;
        } else {
            offset = firstPageSize + (page - 2) * defaultPageSize;
            limit = defaultPageSize;
        }

        // Запрос с параметрами
        List<Folder> folders = folderRepository.findByUserWithOffsetAndLimit(user.getId(), offset, limit);
        List<Long> folderIds = folders.stream().map(Folder::getId).collect(Collectors.toList());
        Map<Long, List<Card>> cardsByFolder = cardRepository.findByFolderIdIn(folderIds)
                .stream()
                .collect(Collectors.groupingBy(card -> card.getFolder().getId()));

        // Устанавливаем карточки для каждой папки
        folders.forEach(f -> f.setCards(cardsByFolder.getOrDefault(f.getId(), Collections.emptyList())));

        List<FolderGetDto> folderDto = folders.stream()
                .map(f -> {
                    int cardCount = f.getCards().size();
                    ReviewTimeDto nearestReviewTime = calculateNearestReviewTime(f.getCards(), Locale.forLanguageTag(lang));
                    return new FolderGetDto(f.getId(), f.getName(), cardCount, nearestReviewTime);
                })
                .collect(Collectors.toList());

        return new FolderPageDto(
                folderDto,
                page,
                totalPages,
                page < totalPages,
                page > 1
        );
    }



    private ReviewTimeDto calculateNearestReviewTime(List<Card> cards, Locale locale) {
        return cards.stream()
                .map(Card::getNextReviewAt)
                .map(nextReviewAt -> reviewTimeService.getTimeUntilNextReview(nextReviewAt, locale))
                .filter(Objects::nonNull)
                .min(Comparator.comparingDouble(ReviewTimeDto::getValue))
                .orElse(null);
    }


    public AllCardsDto getAllCards(String token, String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        Long userId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));

        List<Card> allCards = cardRepository.findByUserId(userId);
        int totalCardCount = allCards.size();

        ReviewTimeDto nearestReview = allCards.stream()
                .map(Card::getNextReviewAt)
                .map(nextReviewAt -> reviewTimeService.getTimeUntilNextReview(nextReviewAt, locale))
                .filter(Objects::nonNull)
                .min(Comparator.comparingDouble(ReviewTimeDto::getValue))
                .orElse(new ReviewTimeDto(0.0,
                        messageSource.getMessage("time.no_review", null, locale)));

        return new AllCardsDto(totalCardCount, nearestReview);
    }

    @Transactional
    public ResponseEntity<?> deleteUserFolder(Long folderId, String token, String lang) {
        Locale locale = Locale.forLanguageTag(lang);

        try {
            Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
            User currentUser = userService.getUserById(currentUserId, lang);

            Folder folder = folderByFolderIdAndUser(folderId, currentUser, lang);

            folderRepository.delete(folder);

            return ResponseEntity.ok(new ErrorResponseDto(null,
                    messageSource.getMessage("folder.delete_success",
                            new Object[]{folder.getName()},
                            locale)));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("folderId",
                            messageSource.getMessage("folder.not_found", null, locale)));
        }
    }



    public ResponseEntity<?> changeNameOfFolder(Long folderId, FolderCreateDto folderCreateDto, String token, String lang){

        try {
            Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));
            User currentUser = userService.getUserById(currentUserId, lang);

            Folder folder = folderByFolderIdAndUser(folderId, currentUser, lang);

            if (folder.getName() != folderCreateDto.getName()){
                folder.setName(folderCreateDto.getName());
                folderRepository.save(folder);
            }

            return ResponseEntity.ok(new ErrorResponseDto(null,
                    messageSource.getMessage("folder.change_name_success",
                            new Object[]{folder.getName()},
                            Locale.forLanguageTag(lang))));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto(
                            "folderId",
                            e.getMessage()
                    ));
        }

    }


    public ResponseEntity haveNotFolders(String lang){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(messageSource.getMessage("folders.not_found", null, Locale.forLanguageTag(lang)));
    }

    private Folder folderByFolderIdAndUser(Long folderId, User user, String lang){
        return folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("folder.not_found", null, Locale.forLanguageTag(lang))
                ));
    }

    public Folder getFolderByUserAndFolderId(User user, Long folderId, String lang){

        Locale locale = Locale.forLanguageTag(lang);

        return folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("folder.not_found", null, locale)
                ));
    }

}


