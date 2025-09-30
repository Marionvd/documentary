package com.mariodoumbanov.documentqa.Controller;

import com.mariodoumbanov.documentqa.DTO.SafeDocDTO;
import com.mariodoumbanov.documentqa.entity.Doc;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<SafeDocDTO> uploadFile(@RequestParam("file") MultipartFile file,
                                                 @AuthenticationPrincipal User user) {
        try {
            Doc savedFile = fileService.save(file, user);
            return ResponseEntity.ok(SafeDocDTO.of(savedFile));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
