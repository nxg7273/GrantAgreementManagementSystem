package com.grantmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GrantManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrantManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner generatePasswordHash(BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            String password = "TestPassword123!";
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println("Hashed password for test user: " + hashedPassword);
        };
    }
}
