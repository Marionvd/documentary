package com.mariodoumbanov.documentqa.repository;

import com.mariodoumbanov.documentqa.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends JpaRepository<Doc, Integer> {

}
