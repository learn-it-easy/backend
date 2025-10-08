package com.system.learn.controller;

import com.system.learn.dto.folder.*;
import com.system.learn.entity.Folder;
import com.system.learn.service.FolderService;
import com.system.learn.utils.CookieUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }


    // создание папки
    @PostMapping("/")
    public ResponseEntity<FolderIdResponse> createFolder(
            @RequestBody FolderCreateDto folderCreateDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {


        Folder createdFolder = folderService.createFolder(folderCreateDto, token, lang);

        return ResponseEntity.ok(new FolderIdResponse(createdFolder.getId(), createdFolder.getName()));
    }

    // получение всех папок постранично
    @GetMapping("/")
    public ResponseEntity<?> getFoldersPaginated(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int page,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        try {
            FolderPageDto result = folderService.getUserFolders(token, page, lang);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new FolderPageDto(
                            Collections.emptyList(),
                            1,
                            1,
                            false,
                            false
                    ));
        }
    }

    // удаление папки
    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(
            @PathVariable Long folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return folderService.deleteUserFolder(folderId, token, lang);
    }

    // изменение названия папки
    @PatchMapping("/{folderId}")
    public ResponseEntity<?> changeNameFolder(
            @PathVariable Long folderId,
            @RequestBody FolderCreateDto folderCreateDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return folderService.changeNameOfFolder(folderId, folderCreateDto, token, lang);
    }

    // Кол-во всех карт и время ближайшей для повторения карточки
    @GetMapping("/stats-all")
    public AllCardsDto getQuantityAndNearestReviewFromAllFolders(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {
        return folderService.getQuantityAndNearestReviewFromAllFolders(token, lang);
    }

    // Вывод всех папок для текущего юзера
    @GetMapping("/all")
    public List<FoldersGetAllDto> getAllFolders(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {
        return folderService.getAllFolders(token, lang);
    }



}
