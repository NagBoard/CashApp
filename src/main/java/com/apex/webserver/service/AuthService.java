package com.apex.webserver.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    /**
     * Authenticates a user with the provided username and password
     *
     * @param username the username
     * @param password the password
     * @return a JWT token if authentication is successful
     * @throws RuntimeException if authentication fails
     */
    public String authenticate(String username, String password) {
        // TODO: Implement actual authentication logic
        // This is a placeholder implementation
        if ("admin".equals(username) && "password".equals(password)) {
            // In a real application, you would:
            // 1. Verify credentials against the database
            // 2. Generate a proper JWT token with appropriate claims
            // 3. Include user roles/permissions in the token
            return "sample-jwt-token-" + System.currentTimeMillis();
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}