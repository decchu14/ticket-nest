package com.ticketnest.user_service.Controller;

import com.ticketnest.user_service.Service.UserService;
import com.ticketnest.user_service.dto.RegisterRequest;
import com.ticketnest.user_service.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request)
    {
        RegisterResponse result = userService.register(request);
        return ResponseEntity.ok(result);
    }
}
