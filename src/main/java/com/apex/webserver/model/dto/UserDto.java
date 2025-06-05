package com.apex.webserver.model.dto;

import java.util.List;

// Nested UserDto class
public class UserDto {
    private Long id;
    private String email;
    private List<String> roles;

    public UserDto() {
    }

    public UserDto(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
