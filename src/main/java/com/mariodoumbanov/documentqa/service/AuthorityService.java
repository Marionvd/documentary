package com.mariodoumbanov.documentqa.service;

import com.mariodoumbanov.documentqa.entity.Authority;
import com.mariodoumbanov.documentqa.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }
    @Transactional
    public void createAuthority(String name, String description) {
        var authority = new Authority(null, name, description, null, new Date(System.currentTimeMillis()));

        authorityRepository.save(authority);
    }
    @Transactional
    public Authority findAuthorityByName(String name) {
        return authorityRepository.findAuthorityByName(name);
    }
}
