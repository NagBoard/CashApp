package com.apex.webserver.model.dto;

import java.util.List;

public class JwtResponseDto {
    private String accessToken;
    private UserDto user;

    // Constructors
    public JwtResponseDto() {
    }

    public JwtResponseDto(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    // Convenience constructor using User entity
    public JwtResponseDto(String accessToken, com.apex.webserver.model.entity.User user, List<String> roles) {
        this.accessToken = accessToken;
        this.user = new UserDto(
                user.getId(),
                user.getEmail(),
                roles
        );
    }

    // Backward compatibility constructor
    public JwtResponseDto(String accessToken, Long id, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.user = new UserDto(id, email, roles);
    }

    // Getters and setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

}