package com.mariodoumbanov.documentqa.DTO;

import com.mariodoumbanov.documentqa.entity.Document;

public record SafeDocDTO(
        Integer id,
        String filename,
        String contentType,
        Long size
) {
    public static SafeDocDTO of(Document document) {
        return new SafeDocDTO(
                document.getId(),
                document.getFilename(),
                document.getContentType(),
                document.getSize()
        );
    }
}
