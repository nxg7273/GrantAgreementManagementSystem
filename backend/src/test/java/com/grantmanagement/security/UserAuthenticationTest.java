package com.grantmanagement.security;

import com.grantmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserAuthenticationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDetails userDetails;
    private String rawPassword;

    @BeforeEach
    public void setup() {
        String username = "testuser@example.com";
        rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userDetails = new User(username, encodedPassword, new ArrayList<>());
        when(userService.loadUserByUsername(username)).thenReturn(userDetails);
    }

    @Test
    public void testSuccessfulAuthentication() {
        // Arrange
        String username = "testuser@example.com";

        System.out.println("Test setup: Username = " + username + ", Raw Password = " + rawPassword);
        System.out.println("Mocked UserDetails: " + userDetails);

        // Act
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, rawPassword)
        );

        // Assert
        System.out.println("Authentication result: " + authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals(username, authentication.getName());
    }

    @Test
    public void testFailedAuthentication() {
        // Arrange
        String username = "testuser@example.com";
        String wrongPassword = "wrongpassword";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, wrongPassword)
            );
        });
    }
}
