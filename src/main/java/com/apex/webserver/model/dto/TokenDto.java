package com.apex.webserver.model.dto;

public class TokenDto {
    private String accessToken;
    private String tokenType = "Bearer";

    public TokenDto() {
    }

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}