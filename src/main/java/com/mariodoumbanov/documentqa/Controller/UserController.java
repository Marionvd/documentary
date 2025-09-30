package com.mariodoumbanov.documentqa.Controller;

import com.mariodoumbanov.documentqa.DTO.PagedResponse;
import com.mariodoumbanov.documentqa.DTO.SafeUserDTO;
import com.mariodoumbanov.documentqa.DTO.UserUpdateDTO;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser(@AuthenticationPrincipal UserDetails principal) {
        if(principal instanceof User user) {
            return ResponseEntity.ok(SafeUserDTO.of(user));
        }

        return ResponseEntity.ok(principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @AuthenticationPrincipal User user, @RequestBody UserUpdateDTO userUpdateDTO) {
        if(!Objects.equals(user.getId(), id)) {
            return ResponseEntity.badRequest().body("Invalid operation. Can only update own account.");
        }
        try {
            if (userUpdateDTO.email() != null && !userUpdateDTO.email().isEmpty()) {
                user = userService.updateEmail(user, userUpdateDTO.email());
                return ResponseEntity.ok(user);
            }
            if(userUpdateDTO.password() != null && !userUpdateDTO.password().isEmpty()) {
                user = userService.updatePassword(user, userUpdateDTO.password(), userUpdateDTO.confirmPassword());
            }
        } catch(Exception ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
        return ResponseEntity.ok(SafeUserDTO.of(user));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<?>> queryUsers(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {
        var users = userService.pageUsers(limit, offset);

        PagedResponse<SafeUserDTO> response = new PagedResponse<>(
                users.getContent().stream().map(SafeUserDTO::of).toList(),
                users.getSize(),
                users.getTotalElements(),
                users.getNumber()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
        } catch(IllegalArgumentException ea) {
            return ResponseEntity.badRequest().body(ea.getMessage());
        }

        return ResponseEntity.ok("Sorry to see you leave.");
    }
}
