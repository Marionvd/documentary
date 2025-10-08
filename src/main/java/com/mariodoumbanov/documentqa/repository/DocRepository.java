package com.mariodoumbanov.documentqa.repository;

import com.mariodoumbanov.documentqa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends JpaRepository<Document, Integer> {

    boolean existsDocByFilenameLikeIgnoreCase(String filename);

    Document findByFilenameLikeIgnoreCase(String filename);

    Document findDocumentById(Integer id);
}
