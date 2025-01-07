package com.bee.cookwithfriends.service;

import com.bee.cookwithfriends.entity.User;
import com.bee.cookwithfriends.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructor for CustomUserDetailsService.
     * @param userRepository The repository for handling user data.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method to load a user by their email.
     * @param email The email of the user to load.
     * @return The user entity.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Find the user by email and throw an exception if not found

        if (user == null) {
            throw new UsernameNotFoundException("Not found!"); // This check is redundant because orElseThrow already handles it
        }

        return user;
    }
}