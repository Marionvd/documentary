package com.mariodoumbanov.documentqa.DTO;

import com.mariodoumbanov.documentqa.entity.Authority;
import com.mariodoumbanov.documentqa.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

public record SafeUserDTO(
        String email,
        Collection<? extends GrantedAuthority> authorities,
        Date createdAt
) {
    public static SafeUserDTO of(User user) {
        return new SafeUserDTO(
                user.getEmail(),
                user.getAuthorities(),
                user.getCreatedAt()
        );
    }
}
