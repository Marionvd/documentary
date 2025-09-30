package com.mariodoumbanov.documentqa.DTO;

public record SafeDocDTO(
        Integer id,
        String filename,
        String contentType,
        Long size
) {
    public static SafeDocDTO of(com.mariodoumbanov.documentqa.entity.Doc doc) {
        return new SafeDocDTO(
                doc.getId(),
                doc.getFilename(),
                doc.getContentType(),
                doc.getSize()
        );
    }
}
