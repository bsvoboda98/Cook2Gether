package com.bee.cookwithfriends.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {
    private int id;
    private UserDTO sender;
    private UserDTO receiver;
}
