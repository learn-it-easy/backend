package com.system.learn.controller;

import com.system.learn.dto.FolderCreateDto;
import com.system.learn.dto.FolderGetDto;
import com.system.learn.dto.FolderIdResponse;
import com.system.learn.dto.FolderPageDto;
import com.system.learn.entity.Folder;
import com.system.learn.service.FolderService;
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
            @CookieValue(value = "lang", defaultValue = "ru") String lang) {


        Folder createdFolder = folderService.createFolder(folderCreateDto, token, lang);

        return ResponseEntity.ok(new FolderIdResponse(createdFolder.getId(), createdFolder.getName()));
    }


    @GetMapping("/get")
    public ResponseEntity<?> getFoldersPaginated(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int page,
            @CookieValue(value = "lang", defaultValue = "ru") String lang) {

        FolderPageDto result = folderService.getUserFolders(token, page, lang);

        if (result.getFolders().isEmpty()) {
            return folderService.haveNotFolders(lang);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/get/all")
    public List<FolderGetDto> getAllFolders(
            @RequestHeader("Authorization") String token,
            @CookieValue(value = "lang", defaultValue = "ru") String lang) {

        return folderService.getAllFolders(token, lang);
    }


    @DeleteMapping("/delete/{folderId}")
    public ResponseEntity<?> deleteFolder(
            @PathVariable Long folderId,
            @RequestHeader("Authorization") String token,
            @CookieValue(value = "lang", defaultValue = "ru") String lang) {

            folderService.deleteUserFolder(folderId, token, lang);
            return ResponseEntity.noContent().build();

    }


}
