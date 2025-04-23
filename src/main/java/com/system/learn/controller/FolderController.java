package com.system.learn.controller;

import com.system.learn.dto.folder.FolderCreateDto;
import com.system.learn.dto.folder.FolderGetDto;
import com.system.learn.dto.folder.FolderIdResponse;
import com.system.learn.dto.folder.FolderPageDto;
import com.system.learn.entity.Folder;
import com.system.learn.service.FolderService;
import com.system.learn.utils.CookieUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }



    @PostMapping("/create")
    public ResponseEntity<FolderIdResponse> createFolder(
            @RequestBody FolderCreateDto folderCreateDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {


        Folder createdFolder = folderService.createFolder(folderCreateDto, token, lang);

        return ResponseEntity.ok(new FolderIdResponse(createdFolder.getId(), createdFolder.getName()));
    }


    @PostMapping("/get")
    public ResponseEntity<?> getFoldersPaginated(
            @RequestHeader("Authorization") String token,
            @RequestParam int page,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        FolderPageDto result = folderService.getUserFolders(token, page, lang);

        if (result.getFolders().isEmpty()) {
            return folderService.haveNotFolders(lang);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/all")
    public List<FolderGetDto> getAllFolders(
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return folderService.getAllFolders(token, lang);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFolder(
            @RequestParam int folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

            return folderService.deleteUserFolder(Long.valueOf(folderId), token, lang);
    }

    @PatchMapping("/changeName")
    public ResponseEntity<?> changeNameFolder(
            @RequestParam int folderId,
            @RequestBody FolderCreateDto folderCreateDto,
            @RequestHeader("Authorization") String token,
            @CookieValue(value= CookieUtils.INTERFACE_LANG_COOKIE, defaultValue = CookieUtils.DEFAULT_LANG_FOR_INTERFACE_COOKIE) String lang) {

        return folderService.changeNameOfFolder(Long.valueOf(folderId), folderCreateDto, token, lang);
    }


}
