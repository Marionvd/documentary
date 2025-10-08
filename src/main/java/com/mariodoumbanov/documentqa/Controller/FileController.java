package com.mariodoumbanov.documentqa.Controller;

import com.mariodoumbanov.documentqa.DTO.SafeDocDTO;
import com.mariodoumbanov.documentqa.entity.Document;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
            Document savedFile = fileService.save(file, user);
            return ResponseEntity.ok(SafeDocDTO.of(savedFile));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getfile(@PathVariable("id") int id) {
        try {
            Resource fileResource = fileService.loadFileAsResource(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable("id") int id, @AuthenticationPrincipal User user) {
        try {
            fileService.deleteFile(id, user);
            return ResponseEntity.ok().body("File deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
