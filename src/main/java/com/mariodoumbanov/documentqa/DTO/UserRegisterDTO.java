package com.mariodoumbanov.documentqa.DTO;

import com.mariodoumbanov.documentqa.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank(message = "Unprovided field - email.")
        @Email(message = "Invalid email format. Must be between 3 and 60 characters long.")
        @Size(max = 60, min = 3, message = "Invalid email length.")
        String email,
        @NotBlank(message = "Unprovided field - password.")
        @Size(max = 60, min = 8, message = "Invalid password length. Must be between 8 and 60 characters long.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,60}$", message = "Invalid password.")
        String password
) {

}
