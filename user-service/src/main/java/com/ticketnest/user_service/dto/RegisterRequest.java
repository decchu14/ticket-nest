package com.ticketnest.user_service.dto;

import com.ticketnest.user_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message="name is required")
    private String name;

    @NotBlank(message="email is required")
    @Email(message="email must be valid")
    private String email;

    @NotBlank(message="password is required")
    private String password;

    @NotNull(message="Role is required")
    private Role role;
}
