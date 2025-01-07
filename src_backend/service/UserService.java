package com.bee.cookwithfriends.service;

import com.bee.cookwithfriends.dto.user.CurrentUserDTO;
import com.bee.cookwithfriends.dto.user.FriendRequestDTO;
import com.bee.cookwithfriends.dto.user.UserDTO;
import com.bee.cookwithfriends.entity.FriendRequest;
import com.bee.cookwithfriends.entity.User;
import com.bee.cookwithfriends.exceptions.FriendshipException;
import com.bee.cookwithfriends.repositories.FriendRequestRepository;
import com.bee.cookwithfriends.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final FriendRequestRepository friendRequestRepository;

    /**
     * Constructor for UserService.
     * @param userRepository The repository for handling user data.
     * @param mapper The ModelMapper for mapping entities to DTOs.
     * @param friendRequestRepository The repository for handling friend request data.
     */
    public UserService(UserRepository userRepository, ModelMapper mapper, FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.friendRequestRepository = friendRequestRepository;
    }

    /**
     * Method to get the currently authenticated user.
     * @return A CurrentUserDTO representing the authenticated user.
     */
    public CurrentUserDTO me() {
        User currentUser = getAuthenticatedUser(); // Get the authenticated user

        return mapper.map(currentUser, CurrentUserDTO.class); // Map the user to a CurrentUserDTO
    }

    /**
     * Method to send a friend request.
     * @param receiverId The ID of the user to whom the friend request is sent.
     * @return A FriendRequestDTO representing the sent friend request.
     * @throws ResponseStatusException If the sender or receiver is not found.
     * @throws FriendshipException If the sender and receiver are the same user, or if the users are already friends, or if the friend request already exists.
     */
    @Transactional
    public FriendRequestDTO sendFriendRequest(int receiverId) {
        User sender = getAuthenticatedUser(); // Get the authenticated user as the sender
        sender = userRepository.findById(sender.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found")); // Ensure the sender exists

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found")); // Ensure the receiver exists

        if (sender == receiver) {
            throw new FriendshipException("Sender and receiver are the same user"); // Check if the sender and receiver are the same
        }

        if (sender.isFriend(receiver)) {
            throw new FriendshipException("User is already stored as friend"); // Check if the users are already friends
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver); // Create a new friend request

        if (checkFriendRequestExists(friendRequest.getSender(), friendRequest.getReceiver())) {
            throw new FriendshipException("FriendRequest already exists"); // Check if the friend request already exists
        }

        FriendRequest request = friendRequestRepository.save(friendRequest); // Save the friend request

        return friendRequestToDTO(request); // Convert the friend request to a DTO
    }

    /**
     * Method to accept a friend request.
     * @param senderId The ID of the user who sent the friend request.
     * @throws FriendshipException If the user is not found or the friend request is not found.
     */
    @Transactional
    public void acceptFriendRequest(int senderId) {
        User acceptor = getAuthenticatedUser(); // Get the authenticated user as the acceptor

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new FriendshipException("User not found")); // Ensure the sender exists

        FriendRequest storedFriendRequest = friendRequestRepository.findBySenderAndReceiver(sender, acceptor)
                .orElseThrow(() -> new FriendshipException("FriendRequest not found")); // Ensure the friend request exists

        sender.addFriend(acceptor); // Add the acceptor to the sender's friends list

        friendRequestRepository.delete(storedFriendRequest); // Delete the friend request

        userRepository.save(sender); // Save the updated sender
    }

    /**
     * Method to search for users by username.
     * @param username The username to search for.
     * @return A list of UserDTO representing the found users.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> searchUsers(String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username); // Find users by username

        return userListToUserDTO(users); // Convert the list of users to a list of UserDTO
    }

    /**
     * Method to get the list of friends for the current user.
     * @return A list of UserDTO representing the friends.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> friends() {
        User user = getAuthenticatedUser(); // Get the authenticated user

        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new FriendshipException("User not found")); // Ensure the user exists

        List<User> friends = user.getFriends();
        friends.addAll(user.getFriendOf()); // Combine friends and friendOf lists

        return userListToUserDTO(friends); // Convert the list of friends to a list of UserDTO
    }

    /**
     * Method to get the list of pending friend requests for the current user.
     * @return A list of UserDTO representing the senders of the friend requests.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getFriendRequests() {
        User user = getAuthenticatedUser(); // Get the authenticated user

        List<User> friendRequests = friendRequestRepository.findSendersByReceiverId(user.getId()); // Find the senders of the friend requests

        return userListToUserDTO(friendRequests); // Convert the list of senders to a list of UserDTO
    }

    /**
     * Method to get the currently authenticated user.
     * @return The authenticated user.
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Get the current authentication context
        return (User) authentication.getPrincipal(); // Get the authenticated user from the context
    }

    /**
     * Method to convert a FriendRequest to a FriendRequestDTO.
     * @param friendRequest The FriendRequest to convert.
     * @return A FriendRequestDTO representing the friend request.
     */
    private FriendRequestDTO friendRequestToDTO(FriendRequest friendRequest) {
        FriendRequestDTO requestDTO = new FriendRequestDTO();

        requestDTO.setId(friendRequest.getId()); // Set the ID of the friend request
        requestDTO.setSender(mapper.map(friendRequest.getSender(), UserDTO.class)); // Set the sender of the friend request
        requestDTO.setReceiver(mapper.map(friendRequest.getReceiver(), UserDTO.class)); // Set the receiver of the friend request

        return requestDTO;
    }

    /**
     * Method to check if a friend request already exists between two users.
     * @param sender The sender of the friend request.
     * @param receiver The receiver of the friend request.
     * @return True if the friend request exists, false otherwise.
     */
    private boolean checkFriendRequestExists(User sender, User receiver) {
        return (
                friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent() // Check if the friend request exists in one direction
                        || friendRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent() // Check if the friend request exists in the opposite direction
        );
    }

    /**
     * Method to convert a list of User entities to a list of UserDTO.
     * @param users The list of User entities.
     * @return A list of UserDTO.
     */
    private List<UserDTO> userListToUserDTO(List<User> users) {
        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : users) {
            usersDTO.add(mapper.map(user, UserDTO.class)); // Map each user to a UserDTO
        }

        return usersDTO;
    }
}