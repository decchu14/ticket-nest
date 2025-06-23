package com.ticketnest.user_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketnest.user_service.Service.UserService;
import com.ticketnest.user_service.dto.RegisterRequest;
import com.ticketnest.user_service.dto.RegisterResponse;
import com.ticketnest.user_service.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception
    {
        //create a fake request object
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("password123");
        request.setRole(Role.USER);

        //Tell the fake user service what to return
        when(userService.register(request)).thenReturn(new RegisterResponse("User registered successfully"));

        //perform the POST request and verify response
        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void shouldFailValidationWhenNameIsMissing() throws Exception
    {
        // create a fake request object without setting name field
        RegisterRequest request = new RegisterRequest();
        request.setEmail("alice@example.com");
        request.setRole(Role.ADMIN);
        request.setPassword("password123");

        //perform a request and verify validation
        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name is required"));
    }

    @Test
    void shouldFailValidationWhenPasswordIsMissing() throws Exception
    {
        // create a fake request object without setting password field
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setRole(Role.SUPPORT_AGENT);

        // perform request and verify validation
        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("password is required"));
    }

    @Test
    void shouldFailValidationWhenEmailIsMissing() throws Exception
    {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setPassword("Password123");
        request.setRole(Role.USER);

        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email is required"));
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() throws Exception
    {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setPassword("Password123");
        request.setEmail("alice-email");
        request.setRole(Role.ADMIN);

        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email must be valid"));
    }

    @Test
    void shouldFailValidationWhenEmailIsDuplicate() throws Exception
    {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setPassword("Password123");
        request.setEmail("alice@example.com");
        request.setRole(Role.ADMIN);

        when(userService.register(request)).thenThrow(new IllegalArgumentException("Email is already taken"));

        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is already taken"));
    }

    @Test
    void shouldFailValidationWhenRoleIsMissing() throws Exception
    {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPassword("Password123");

        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Role is required"));

    }

    @Test
    void shouldFailValidationWhenRoleIsInvalid() throws Exception
    {
        String request = """
                {
                    "name":"Alice",
                    "email":"alice@example.com",
                    "password":"password123",
                    "role":"MANAGER"
                
                }
                """;

                mockmvc.perform(post("/api/users/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldFailWhenRequestBodyIsEmpty() throws Exception
    {
        RegisterRequest request = new RegisterRequest();

        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldFailWhenServiceThrowsGenericException() throws Exception
    {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice");
        request.setPassword("Password123");
        request.setEmail("alice@example.com");
        request.setRole(Role.ADMIN);

        when(userService.register(request)).thenThrow(new RuntimeException("something went wrong"));

        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("something went wrong"));

    }

    @Test
    void shouldIgnoreExtraFieldsInRequest() throws Exception
    {
        String request = """
        {
          "name": "Alice",
          "email": "alice@example.com",
          "password": "password123",
          "role": "USER",
          "extraField": "someValue",
          "anotherExtra": 42
        }
        """;

        // We mock the service to return a successful response
        when(userService.register(any(RegisterRequest.class)))
                .thenReturn(new RegisterResponse("User registered successfully"));

        // Perform the request
        mockmvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }




}
