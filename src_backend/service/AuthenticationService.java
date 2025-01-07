package com.bee.cookwithfriends.service;

import com.bee.cookwithfriends.dto.user.LoginUserDTO;
import com.bee.cookwithfriends.dto.user.RegisterUserDTO;
import com.bee.cookwithfriends.entity.User;
import com.bee.cookwithfriends.exceptions.EmailTakenException;
import com.bee.cookwithfriends.exceptions.UsernameTakenException;
import com.bee.cookwithfriends.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor for AuthenticationService.
     * @param userRepository The repository for handling user data.
     * @param authenticationManager The manager for handling authentication.
     * @param passwordEncoder The encoder for handling password encryption.
     */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method to register a new user.
     * @param input The DTO containing the user registration data.
     * @return The registered user.
     * @throws UsernameTakenException If the username is already taken.
     * @throws EmailTakenException If the email is already in use.
     */
    public User signup(RegisterUserDTO input) throws UsernameTakenException, EmailTakenException {
        checkUsernameAvailability(input.getUsername()); // Check if the username is available
        checkEmailAvailability(input.getEmail()); // Check if the email is available

        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword())); // Encode the password before saving

        return userRepository.save(user); // Save the user to the repository
    }

    /**
     * Method to authenticate a user.
     * @param input The DTO containing the user login credentials.
     * @return The authenticated user.
     */
    public User authenticate(LoginUserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(), // Use email as the username for authentication
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail()) // Find the user by email
                .orElseThrow(); // Throw an exception if the user is not found
    }

    /**
     * Method to check if a username is available.
     * @param username The username to check.
     * @throws UsernameTakenException If the username is already taken.
     */
    private void checkUsernameAvailability(String username) throws UsernameTakenException {
        boolean exists = userRepository.existsByUsername(username); // Check if the username exists
        if (exists) throw new UsernameTakenException("Username " + username + " is already taken"); // Throw an exception if it does
    }

    /**
     * Method to check if an email is available.
     * @param email The email to check.
     * @throws EmailTakenException If the email is already in use.
     */
    private void checkEmailAvailability(String email) throws EmailTakenException {
        boolean exists = userRepository.existsByEmail(email); // Check if the email exists
        if (exists) throw new EmailTakenException("Email is already in use"); // Throw an exception if it does
    }
}