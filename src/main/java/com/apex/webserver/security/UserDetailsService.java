package com.apex.webserver.security;

import com.apex.webserver.model.entity.User;
import com.apex.webserver.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Implementation of {@code org.springframework.security.core.userdetails.UserDetailsService}
 * <p>This class is used to load user details from the database.</p>
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    // Dependency injection
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates Spring Security user from my User entity and frame it as spring User class, spring User is implementation of UserDetails interface
     * @param email
     * @return {@code UserDetails} object containing our internal logic user information, including username, password, authorities, etc.
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                // Set username to email
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                // fetching user roles
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()))
                .build();
    }
}