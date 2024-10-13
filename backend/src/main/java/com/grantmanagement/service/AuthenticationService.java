package com.grantmanagement.service;

import com.grantmanagement.model.Participant;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    String login(String email, String password);
    void logout(String token);
    String refreshToken(String token);
    boolean validateToken(String token);
    Participant getCurrentUser(String token);
    boolean changePassword(String token, String oldPassword, String newPassword);
    String resetPassword(String email);
}
