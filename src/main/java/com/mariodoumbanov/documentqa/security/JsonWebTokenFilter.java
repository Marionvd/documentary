package com.mariodoumbanov.documentqa.security;

import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.service.JsonWebTokenService;
import com.mariodoumbanov.documentqa.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JsonWebTokenFilter extends OncePerRequestFilter {
    private final JsonWebTokenService jsonWebTokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JsonWebTokenFilter(JsonWebTokenService jsonWebTokenService, UserDetailsService theUserDetailsService) {
        this.jsonWebTokenService=jsonWebTokenService;
        userDetailsService = theUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tkn = request.getHeader("Authorization");
        if (tkn == null || tkn.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = null;
        if (tkn.startsWith("Bearer ")) {
            token = tkn.substring(7);
        }
        try {
            String email = jsonWebTokenService.validate(token);

            if(email == null) {
                filterChain.doFilter(request, response);
                return;
            }

            User user = (User) userDetailsService.loadUserByUsername(email);
            if(user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
