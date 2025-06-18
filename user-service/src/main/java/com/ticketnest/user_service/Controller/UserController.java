package com.ticketnest.user_service.Controller;

import com.ticketnest.user_service.Service.UserService;
import com.ticketnest.user_service.dto.RegisterRequest;
import com.ticketnest.user_service.dto.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request)
    {
        RegisterResponse result = userService.register(request);
        return ResponseEntity.ok(result);
    }
}
