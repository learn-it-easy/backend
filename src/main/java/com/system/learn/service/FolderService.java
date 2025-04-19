package com.system.learn.service;

import com.system.learn.dto.FolderCreateDto;
import com.system.learn.dto.FolderGetDto;
import com.system.learn.dto.FolderPageDto;
import com.system.learn.entity.Folder;
import com.system.learn.entity.User;
import com.system.learn.repository.FolderRepository;
import com.system.learn.security.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class FolderService {

    @Autowired
    private MessageSource messageSource;
    private final UserService userService;
    private final FolderRepository folderRepository;
    private final JwtUtils jwtUtils;


    public FolderService(UserService userService, FolderRepository folderRepository, JwtUtils jwtUtils) {
        this.userService = userService;
        this.folderRepository = folderRepository;
        this.jwtUtils = jwtUtils;
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

    public List<FolderGetDto> getAllFolders(String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));

        User user = userService.getUserById(currentUserId, lang);

        List<Folder> folders = folderRepository.findByUser(user);
        return folders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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

        List<FolderGetDto> folderDto = folders.stream()
                .map(f -> new FolderGetDto(f.getId(), f.getName()))
                .collect(Collectors.toList());

        return new FolderPageDto(
                folderDto,
                page,
                totalPages,
                page < totalPages,
                page > 1
        );
    }

    @Transactional
    public void deleteUserFolder(Long folderId, String token, String lang) {

        Long currentUserId = jwtUtils.getUserIdFromToken(jwtUtils.cleanToken(token));

        User currentUser = userService.getUserById(currentUserId, lang);

        Folder folder = getFolderById(folderId, currentUser, lang, "folder.not_found_to_delete");

        folderRepository.delete(folder);
    }


    private Folder getFolderById(Long folderId, User currentUser, String lang, String messageIfNotFound) {
        Locale locale = Locale.forLanguageTag(lang);
        return folderRepository.findByIdAndUser(folderId, currentUser)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage(messageIfNotFound, null, locale)));
    }

    private FolderGetDto convertToDto(Folder folder) {
        return new FolderGetDto(folder.getId(), folder.getName());
    }


    public ResponseEntity haveNotFolders(String lang){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(messageSource.getMessage("folders.not_found", null, Locale.forLanguageTag(lang)));
    }

}


