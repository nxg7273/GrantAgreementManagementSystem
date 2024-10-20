package com.grantmanagement.controller;

import com.grantmanagement.security.JwtTokenUtil;
import com.grantmanagement.service.UserService;
import com.grantmanagement.model.User;
import com.grantmanagement.dto.SignupRequest;
import com.grantmanagement.dto.LoginRequest;
import com.grantmanagement.dto.JwtResponse;
import com.grantmanagement.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        User user = userService.registerUser(signupRequest);
        return ResponseEntity.ok().body(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    // Temporary endpoint to update testuser password
    @PostMapping("/update-testuser-password")
    public ResponseEntity<?> updateTestUserPassword() {
        userService.updateUserPassword("testuser", "newTestPassword");
        return ResponseEntity.ok(new MessageResponse("Testuser password updated successfully"));
    }
}

// Remove the duplicate JwtRequest and UserRegistrationRequest classes as they are now in separate DTO files
