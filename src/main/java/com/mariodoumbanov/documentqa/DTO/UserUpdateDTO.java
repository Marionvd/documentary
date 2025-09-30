package com.mariodoumbanov.documentqa.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min=5, max=100, message="Invalid email length")
        @Email(message = "Invalid email format")
        String email,

        @Size(min=8, max=30, message="Invalid password length. Password must be between 8 and 30 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String password,

        @NotBlank(message="Unprovided fields - confirmation password")
        String confirmPassword
) {
}
