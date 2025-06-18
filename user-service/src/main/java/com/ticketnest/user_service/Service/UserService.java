package com.ticketnest.user_service.Service;

import com.ticketnest.user_service.Repository.UserRepository;
import com.ticketnest.user_service.dto.RegisterRequest;
import com.ticketnest.user_service.dto.RegisterResponse;
import com.ticketnest.user_service.model.User;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest request)
    {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole()).build();

        userRepository.save(user);
        return new RegisterResponse("User saved successfully");
    }

}
