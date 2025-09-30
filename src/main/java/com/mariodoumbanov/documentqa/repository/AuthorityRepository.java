package com.mariodoumbanov.documentqa.repository;

import com.mariodoumbanov.documentqa.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Authority findAuthorityByName(String name);
}
