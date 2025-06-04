package com.apex.webserver.model.dto;

public class LoginResponseDto {
    private String access_token;
    private String token_type;
    private int expires_in;

    // Constructor, getters, setters
    public LoginResponseDto(String accessToken, String tokenType, int expiresIn) {
        this.access_token = accessToken;
        this.token_type = tokenType;
        this.expires_in = expiresIn;
    }

    // Getters
    public String getAccess_token() { return access_token; }
    public String getToken_type() { return token_type; }
    public int getExpires_in() { return expires_in; }
}