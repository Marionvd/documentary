package com.mariodoumbanov.documentqa.security;

import com.mariodoumbanov.documentqa.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final JsonWebTokenFilter jsonWebTokenFilter;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService, JsonWebTokenFilter jsonWebTokenFilter) {
        this.userDetailsService=userDetailsService;
        this.jsonWebTokenFilter=jsonWebTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**", "/api/files/**").authenticated()
                        .anyRequest().denyAll()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jsonWebTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
