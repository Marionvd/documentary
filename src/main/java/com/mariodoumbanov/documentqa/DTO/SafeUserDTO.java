package com.mariodoumbanov.documentqa.DTO;

import com.mariodoumbanov.documentqa.entity.Authority;
import com.mariodoumbanov.documentqa.entity.User;

import java.util.Date;
import java.util.Set;

public record SafeUserDTO(
        String email,
        Set<Authority> authorities,
        Date createdAt,
) {
    public static SafeUserDTO of(User user) {
        return new SafeUserDTO(
                user.getEmail(),
                (Set<Authority>) user.getAuthorities(),
                user.getCreatedAt()
        );
    }
}
