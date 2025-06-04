package com.apex.webserver.model.dto;

public class LoginResponseDto {
    private final String accessToken;
    private final String tokenType;
    private final int expiresIn;

    // Constructor, getters, setters
    public LoginResponseDto(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public int getExpiresIn() { return expiresIn; }
}