package com.ticketnest.user_service.Service;

import com.ticketnest.user_service.Repository.UserRepository;
import com.ticketnest.user_service.dto.RegisterRequest;
import com.ticketnest.user_service.dto.RegisterResponse;
import com.ticketnest.user_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public RegisterResponse register(RegisterRequest request)
    {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole()).build();

        try {
            userRepository.save(user);
        }
        catch(Exception e)
        {
            return new RegisterResponse("Failed to register user due to : "+e.getMessage());
        }
        return new RegisterResponse("User registered successfully");

    }

}
