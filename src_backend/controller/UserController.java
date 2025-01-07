package com.bee.cookwithfriends.controller;

import com.bee.cookwithfriends.dto.user.CurrentUserDTO;
import com.bee.cookwithfriends.dto.user.FriendRequestDTO;
import com.bee.cookwithfriends.dto.user.UserDTO;
import com.bee.cookwithfriends.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling user-related endpoints.
 */
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    /**
     * Constructor for UserController.
     * @param userService The service for handling user operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to get the currently authenticated user.
     * @return A ResponseEntity containing the current user's data.
     */
    @GetMapping("/me")
    public ResponseEntity<CurrentUserDTO> authenticatedUser() {
        return ResponseEntity.ok(userService.me()); // Fetch the current user's data from the service
    }

    /**
     * Endpoint to send a friend request to another user.
     * @param receiverId The ID of the user to whom the friend request is sent.
     * @return A ResponseEntity containing the friend request data.
     */
    @PostMapping("/sendFriendRequest/{receiverId}")
    public ResponseEntity<FriendRequestDTO> sendFriendRequest(@PathVariable int receiverId) {
        FriendRequestDTO requestDTO = userService.sendFriendRequest(receiverId); // Send the friend request and get the request data
        return ResponseEntity.ok(requestDTO);
    }

    /**
     * Endpoint to accept a friend request from another user.
     * @param senderId The ID of the user who sent the friend request.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/acceptFriendRequest/{senderId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable int senderId) {
        userService.acceptFriendRequest(senderId); // Accept the friend request
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return a 204 No Content status
    }

    /**
     * Endpoint to search for users by username.
     * @param username The username to search for.
     * @return A ResponseEntity containing a list of matching users.
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String username) {
        List<UserDTO> users = userService.searchUsers(username); // Search for users by username
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint to get the list of friends for the current user.
     * @return A ResponseEntity containing a list of friends.
     */
    @GetMapping("/friends")
    public ResponseEntity<List<UserDTO>> friends() {
        List<UserDTO> friends = userService.friends(); // Get the list of friends
        return ResponseEntity.ok(friends);
    }

    /**
     * Endpoint to get the list of pending friend requests for the current user.
     * @return A ResponseEntity containing a list of friend requests.
     */
    @GetMapping("/friendRequests")
    public ResponseEntity<List<UserDTO>> friendRequests() {
        List<UserDTO> friendRequests = userService.getFriendRequests(); // Get the list of pending friend requests
        return ResponseEntity.ok(friendRequests);
    }
}