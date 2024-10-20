package com.grantmanagement.service.impl;

import com.grantmanagement.model.User;
import com.grantmanagement.repository.UserRepository;
import com.grantmanagement.service.UserService;
import com.grantmanagement.dto.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import java.util.ArrayList;

@Service
@Primary
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(SignupRequest signupRequest) {
        System.out.println("Attempting to register user: " + signupRequest.getUsername());
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        System.out.println("User object created, attempting to save to repository");
        try {
            User savedUser = userRepository.save(user);
            System.out.println("User successfully saved: " + savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Add a new method to generate BCrypt hash
    public String generateBCryptHash(String password) {
        return passwordEncoder.encode(password);
    }

    // Add a new method to generate BCrypt hash and update user password
    @Override
    public void updateUserPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            System.out.println("Password updated for user: " + username);
        } else {
            System.out.println("User not found: " + username);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        System.out.println("User found: " + username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
