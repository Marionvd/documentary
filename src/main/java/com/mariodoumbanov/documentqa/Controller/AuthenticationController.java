package com.mariodoumbanov.documentqa.Controller;

import com.mariodoumbanov.documentqa.DTO.UserLoginDTO;
import com.mariodoumbanov.documentqa.DTO.UserRegisterDTO;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.service.JsonWebTokenService;
import com.mariodoumbanov.documentqa.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final JsonWebTokenService jwtService;
    public AuthenticationController(UserService userService, JsonWebTokenService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO, HttpServletResponse response) {
        User user;
        try {
            user = userService.register(userRegisterDTO);
        } catch(IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }

        String token = jwtService.generate(user.getId(),user.getEmail());

        generateCookie(response, token);

        return ResponseEntity.created(URI.create("/api/users/" + user.getId()))
                .header("Authorization", "Bearer "+token).body(user);
    }

    private void generateCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        User user;
        try {
            user = userService.login(userLoginDTO);
        } catch(IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }

        String token = jwtService.generate(user.getId(),user.getEmail());

        generateCookie(response, token);

        var res = ResponseEntity.ok().header("Authorization", "Bearer "+token).body(user);

        return res;
    }
}
