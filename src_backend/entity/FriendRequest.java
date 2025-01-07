package com.bee.cookwithfriends.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="friend_request")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", referencedColumnName = "id")
    private User receiver;

    public FriendRequest(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
    }
}
