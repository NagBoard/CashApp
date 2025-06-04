package com.apex.webserver.controller;

import com.apex.webserver.model.dto.JwtResponseDto;
import com.apex.webserver.model.dto.LoginRequestDto;
import com.apex.webserver.model.dto.RegisterRequestDto;
import com.apex.webserver.model.dto.TokenDto;
import com.apex.webserver.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
// Remove the CrossOrigin annotation - we'll handle this in WebConfig
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateUser(
            @RequestBody LoginRequestDto loginRequest,
            HttpServletResponse response) {
        JwtResponseDto jwtResponse = authService.authenticateUser(loginRequest, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> registerUser(
            @RequestBody RegisterRequestDto registerRequest,
            HttpServletResponse response) {
        JwtResponseDto jwtResponse = authService.registerUser(registerRequest, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request) {
        // Extract refresh token from cookies
        String refreshToken = null;
        if (request.getCookies() != null) {
            Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(refreshTokenCookieName))
                    .findFirst();

            if (refreshTokenCookie.isPresent()) {
                refreshToken = refreshTokenCookie.get().getValue();
            }
        }

        // Generate new access token
        TokenDto tokenDto = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }
}