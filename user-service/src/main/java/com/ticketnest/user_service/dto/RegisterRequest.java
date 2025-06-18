package com.ticketnest.user_service.dto;

import com.ticketnest.user_service.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
