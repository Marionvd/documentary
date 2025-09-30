package com.mariodoumbanov.documentqa.DTO;

import jakarta.validation.constraints.*;

public record UserUpdateDTO(
        @Null
        @Size(min=5, max=100, message="Invalid email length")
        @Email(message = "Invalid email format")
        String email,

        @Null
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
