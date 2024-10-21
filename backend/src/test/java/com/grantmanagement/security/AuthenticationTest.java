package com.grantmanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grantmanagement.dto.LoginRequest;
import com.grantmanagement.dto.SignupRequest;
import com.grantmanagement.model.User;
import com.grantmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void testUserRegistration() throws Exception {
        logger.info("Starting testUserRegistration");
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");

        logger.debug("Created SignupRequest: {}", signupRequest);

        when(userService.registerUser(any(SignupRequest.class))).thenReturn(new User());
        logger.debug("Mocked userService.registerUser to return a new User");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        logger.info("testUserRegistration completed successfully");
    }

    @Test
    public void testUserLogin() throws Exception {
        logger.info("Starting testUserLogin");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        logger.debug("Created LoginRequest: {}", loginRequest);

        when(jwtTokenUtil.generateToken(any())).thenReturn("test-jwt-token");
        logger.debug("Mocked jwtTokenUtil.generateToken to return 'test-jwt-token'");

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));

        logger.info("testUserLogin completed successfully");
    }
}
