package com.apex.webserver.service;

import com.apex.webserver.model.entity.Role;
import com.apex.webserver.model.entity.User;
import com.apex.webserver.model.dto.JwtResponseDto;
import com.apex.webserver.model.dto.LoginRequestDto;
import com.apex.webserver.model.dto.RegisterRequestDto;
import com.apex.webserver.model.dto.TokenDto;
import com.apex.webserver.repository.RoleRepository;
import com.apex.webserver.repository.UserRepository;
import com.apex.webserver.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public JwtResponseDto authenticateUser(LoginRequestDto loginRequest, HttpServletResponse response) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user details
        org.springframework.security.core.userdetails.UserDetails userDetails =
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate access token (short-lived, returned in response)
        String accessToken = tokenProvider.generateAccessToken(userDetails);

        // Generate refresh token (long-lived, stored in HttpOnly cookie)
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);

        // Create refresh token cookie
        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // For HTTPS
        refreshTokenCookie.setPath("/api/auth"); // Only accessible by auth endpoints
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpiration / 1000)); // Convert from ms to seconds

        // Add cookie to response
        response.addCookie(refreshTokenCookie);

        // Get user roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Return JWT response with access token
        return new JwtResponseDto(
                accessToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
    }

    @Transactional
    public JwtResponseDto registerUser(RegisterRequestDto registerRequest, HttpServletResponse response) {
        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setIsActive(true);

        // Assign default USER role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        roles.add(userRole);
        user.setRoles(roles);

        // Save user
        userRepository.save(user);

        // Authenticate and generate token
        LoginRequestDto loginRequest = new LoginRequestDto(
                registerRequest.getUsername(),
                registerRequest.getPassword()
        );

        return authenticateUser(loginRequest, response);
    }

    public TokenDto refreshToken(String refreshToken) {
        // Validate refresh token
        if (refreshToken == null) {
            throw new RuntimeException("Refresh token is missing");
        }

        // Extract username from refresh token
        String username = tokenProvider.extractUsername(refreshToken);

        // Validate token
        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Load user details
        org.springframework.security.core.userdetails.UserDetails userDetails =
                userRepository.findByUsername(username)
                        .map(user -> org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPasswordHash())
                                .authorities(user.getRoles().stream()
                                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.getName()))
                                        .collect(Collectors.toList()))
                                .build())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate refresh token
        if (!tokenProvider.isTokenValid(refreshToken, userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Generate new access token
        String newAccessToken = tokenProvider.generateAccessToken(userDetails);

        // Return new access token
        return new TokenDto(newAccessToken);
    }

    public void logout(HttpServletResponse response) {
        // Create cookie to invalidate refresh token
        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/api/auth");
        refreshTokenCookie.setMaxAge(0); // Expire immediately

        // Add cookie to response
        response.addCookie(refreshTokenCookie);
    }
}