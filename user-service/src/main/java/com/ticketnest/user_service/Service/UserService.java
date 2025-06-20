package com.ticketnest.user_service.Service;

import com.ticketnest.user_service.Repository.UserRepository;
import com.ticketnest.user_service.dto.RegisterRequest;
import com.ticketnest.user_service.dto.RegisterResponse;
import com.ticketnest.user_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request)
    {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .role(request.getRole()).build();

        try {
            userRepository.save(user);
        }
        catch(DataIntegrityViolationException e)
        {
            throw new IllegalArgumentException("Email is already taken");
        }
        catch(Exception e)
        {
            throw new RuntimeException("Failed to register user due to : "+e.getMessage());
        }
        return new RegisterResponse("User registered successfully");
    }

}
