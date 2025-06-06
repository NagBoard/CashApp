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
import com.apex.webserver.security.UserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserDetailsService userDetailsService;
    private final String TEMP_PASSWORD = ""

    @Value("${jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    // Inject dependencies
    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public JwtResponseDto authenticateUser(LoginRequestDto loginRequest, HttpServletResponse response) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Get user by email (if not found, throw exception), username is defined as email in my UserDetailsService
        User user = userRepository.findByEmail(userDetails.getUsername())
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

        // Get user roles directly from entity (clean names)
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        // Return JWT response with access token
        return new JwtResponseDto(accessToken, user, roles);
    }

    @Transactional
    public JwtResponseDto registerUser(RegisterRequestDto registerRequest, HttpServletResponse response) {

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setIsActive(true);

        // Assign default USER role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found")); // TODO: Handle default role not found with a proper exception
        roles.add(userRole);
        user.setRoles(roles);

        // Save user
        userRepository.save(user);

        // Authenticate and generate token
        LoginRequestDto loginRequest = new LoginRequestDto(
                registerRequest.getEmail(),
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
        String email = tokenProvider.extractUsername(refreshToken);

        // Validate token
        if (email == null) {
            throw new RuntimeException("Invalid refresh token, email is missing");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

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