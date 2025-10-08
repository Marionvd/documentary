package com.mariodoumbanov.documentqa.service;

import com.mariodoumbanov.documentqa.entity.Document;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.repository.DocRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private static final String FILE_DIR = "uploads";
    private final DocRepository docRepository;

    @Autowired
    public FileService(DocRepository docRepository) {
        this.docRepository = docRepository;
    }

    @Transactional
    public Document save(MultipartFile file, User owner) {
        try {
            String filePath = FILE_DIR + "/" + owner.getId().toString();
            Path path = Paths.get(filePath);

            if(!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String filename=owner.getId().toString()+"_"+file.getOriginalFilename();

            Path fileDir = path.resolve(filename);
            Document fileEntity = new Document();
            fileEntity.setFilename(filename);
            fileEntity.setSize(file.getSize());
            fileEntity.setOwner(owner);
            fileEntity.setContentType(file.getContentType());

            if (docRepository.existsDocByFilenameLikeIgnoreCase(filename)) {
                return docRepository.findByFilenameLikeIgnoreCase(filename);
            }
            fileEntity = docRepository.save(fileEntity);
            Files.copy(file.getInputStream(), fileDir, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public Document getDocumentById(int id) {
        if (docRepository.existsById(id)) {
            return docRepository.findDocumentById(id);
        }
        return null;
    }

    @Transactional
    public Resource loadFileAsResource(Document document) {
        return getResource(document);
    }
    @Transactional
    public Resource loadFileAsResource(int documentId) {
        Document document = getDocumentById(documentId);

        return getResource(document);
    }

    private Resource getResource(Document document) {
        try {
            String filePath = FILE_DIR + "/" + document.getOwner().getId() + "/" + document.getFilename();
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + document.getFilename());
            }
        } catch (Exception ex) {
            throw new RuntimeException("File not found: " + document.getFilename(), ex);
        }
    }

    @Transactional
    public void deleteFile(int documentId, User user) {
        Document document = getDocumentById(documentId);
        if(document == null) {
            throw new RuntimeException("File not found");
        }
        if(!document.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this file");
        }
        String filePath = FILE_DIR + "/" + document.getOwner().getId() + "/" + document.getFilename();
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        try {
            Files.deleteIfExists(path);
            docRepository.delete(document);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + document.getFilename(), e);
        }
    }
}
