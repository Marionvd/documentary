package com.mariodoumbanov.documentqa.service;

import com.mariodoumbanov.documentqa.entity.Doc;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.repository.DocRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileService {
    private static final String FILE_DIR = "uploads";
    private final DocRepository docRepository;

    @Autowired
    public FileService(DocRepository docRepository) {
        this.docRepository = docRepository;
    }

    @Transactional
    public Doc save(MultipartFile file, User owner) {
        try {
            String filePath = FILE_DIR + "/" + owner.getId().toString();
            Path path = Paths.get(filePath);

            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path fileDir = path.resolve(owner.getId().toString()+"_"+file.getOriginalFilename());
            Doc fileEntity = new Doc();
            fileEntity.setFilename(owner.getId().toString()+"_"+file.getOriginalFilename());
            fileEntity.setSize(file.getSize());
            fileEntity.setOwner(owner);
            fileEntity.setContentType(file.getContentType());

            fileEntity = docRepository.save(fileEntity);
            Files.copy(file.getInputStream(), fileDir, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
