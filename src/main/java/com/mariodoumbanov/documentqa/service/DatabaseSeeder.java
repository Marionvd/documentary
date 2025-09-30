package com.mariodoumbanov.documentqa.service;

import com.mariodoumbanov.documentqa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseSeeder {
    private final AuthorityService authorityService;
    private final UserService userService;
    @Autowired
    public DatabaseSeeder(AuthorityService authorityService, UserService userService) {
        this.authorityService=authorityService;
        this.userService=userService;
    }

    @Bean
    public CommandLineRunner seedAuthorities() {
        return _ -> {
            List<Map<String, String>> authorities_data = List.of(
                    Map.of("name", "ROLE_USER", "description", "Basic user role"),
                    Map.of("name", "ROLE_ADMIN", "description", "OP admin role")
            );
            for (Map<String, String> authority_data : authorities_data) {
                authorityService.createAuthority(authority_data.get("name"), authority_data.get("description"));
            }

            userService.register("mario@gmail.com", "Password123@");
        };
    }
}
