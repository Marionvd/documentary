package com.mariodoumbanov.documentqa.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @NotBlank(message="Unprovided fields - email")
        @Email(message="Invalid email format")
        String email,
        @NotBlank(message="Unprovided fields - email")
        String password
) {
}
