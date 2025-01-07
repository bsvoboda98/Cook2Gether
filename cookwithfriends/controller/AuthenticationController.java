package com.bee.cookwithfriends.controller;

import com.bee.cookwithfriends.config.JwtService;
import com.bee.cookwithfriends.dto.user.LoginResponse;
import com.bee.cookwithfriends.dto.user.LoginUserDTO;
import com.bee.cookwithfriends.dto.user.RegisterUserDTO;
import com.bee.cookwithfriends.entity.User;
import com.bee.cookwithfriends.exceptions.NoRefreshTokenException;
import com.bee.cookwithfriends.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related endpoints.
 */
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Constructor for AuthenticationController.
     * @param jwtService The service for handling JWT operations.
     * @param authenticationService The service for handling user authentication and registration.
     */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint for user registration.
     * @param registerUserDTO The DTO containing user registration data.
     * @return A ResponseEntity containing the login response with the access token and expiration time.
     */
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterUserDTO registerUserDTO) {
        User registeredUser = authenticationService.signup(registerUserDTO); // Register the user
        String jwtToken = jwtService.generateAccessToken(registeredUser); // Generate access token

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken); // Set the token in the response
        loginResponse.setExpires(jwtService.getAccessExpirationTime()); // Set the token expiration time

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Endpoint for user login.
     * @param loginUserDTO The DTO containing user login credentials.
     * @return A ResponseEntity containing the login response with the access token, expiration time, and refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDTO) {
        User authenticatedUser = authenticationService.authenticate(loginUserDTO); // Authenticate the user

        // Create access token and store it in the login response
        String jwtAccessToken = jwtService.generateAccessToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtAccessToken);
        loginResponse.setExpires(jwtService.getAccessExpirationTime());

        // Create refresh token and store it in an HttpOnly cookie
        String jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUser);
        ResponseCookie cookie = ResponseCookie.from("jwtRefreshToken", jwtRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge((jwtService.getRefreshExpirationTime() / 1000))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", cookie.toString());

        return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK); // Return the response entity with the cookie
    }

    /**
     * Endpoint for refreshing the access token using a refresh token.
     * @param request The HttpServletRequest object.
     * @return A ResponseEntity containing the new login response with the refreshed access token and expiration time.
     * @throws NoRefreshTokenException If no refresh token is found.
     */
    @GetMapping("/refreshToken")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<LoginResponse> refreshToken(HttpServletRequest request) throws NoRefreshTokenException {
        return useRefreshToken(request); // Delegate to the useRefreshToken method
    }

    /**
     * Endpoint for checking if the user is logged in.
     * @param request The HttpServletRequest object.
     * @return A ResponseEntity containing the login response with the refreshed access token and expiration time.
     * @throws NoRefreshTokenException If no refresh token is found.
     */
    @GetMapping("/checkLogin")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<LoginResponse> checkLogin(HttpServletRequest request) throws NoRefreshTokenException {
        return useRefreshToken(request); // Delegate to the useRefreshToken method
    }

    /**
     * Endpoint for logging out the user by invalidating the refresh token.
     * @param request The HttpServletRequest object.
     * @return A ResponseEntity indicating a successful logout.
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Create an empty HttpOnly cookie with a max age of 0 to invalidate the refresh token
        ResponseCookie cookie = ResponseCookie.from("jwtRefreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    /**
     * Private method to use the refresh token to get a new access token.
     * @param request The HttpServletRequest object.
     * @return A ResponseEntity containing the new login response with the refreshed access token and expiration time.
     * @throws NoRefreshTokenException If no refresh token is found.
     */
    private ResponseEntity<LoginResponse> useRefreshToken(HttpServletRequest request) {
        String refreshToken = getCookie(request, "jwtRefreshToken"); // Get the refresh token from the cookies

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new NoRefreshTokenException("No refresh token found"); // Throw an exception if no refresh token is found
        }

        String jwtToken = jwtService.refreshAccessToken(refreshToken); // Refresh the access token

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken); // Set the new token in the response
        loginResponse.setExpires(jwtService.getAccessExpirationTime()); // Set the new token expiration time

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Private method to retrieve a cookie by name from the request.
     * @param request The HttpServletRequest object.
     * @param name The name of the cookie to retrieve.
     * @return The value of the cookie if found, otherwise null.
     */
    private String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies(); // Get all cookies from the request

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) { // Check if the cookie matches the name
                    return cookie.getValue(); // Return the cookie value
                }
            }
        }
        return null; // Return null if the cookie is not found
    }
}