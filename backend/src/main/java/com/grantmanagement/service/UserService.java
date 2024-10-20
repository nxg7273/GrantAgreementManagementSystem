package com.grantmanagement.service;

import com.grantmanagement.model.User;
import com.grantmanagement.dto.SignupRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerUser(SignupRequest signupRequest);
    User findByUsername(String username);
    void updateUserPassword(String username, String newPassword);
}
