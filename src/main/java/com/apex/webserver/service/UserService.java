package com.apex.webserver.service;

@Service
public class UserService {

    public Page<UserDto> findUsers(String search, Pageable pageable) {
        Page<User> users;

        if (search != null && !search.isEmpty()) {
            users = userRepository.findByEmailContainingIgnoreCase(search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(this::convertToDto);
    }
}