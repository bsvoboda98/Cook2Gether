package com.bee.cookwithfriends.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User implements UserDetails {

    //Id of the user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    //email of the user
    @Column(unique = true, length = 100, nullable = false)
    private String username;

    //email of the user
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    //password of the user - encrypted
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="friends",
            joinColumns = @JoinColumn(name="person_id"),
            inverseJoinColumns = @JoinColumn(name="friend_id")
    )
    private List<User> friends;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="friends",
            joinColumns = @JoinColumn(name="friend_id"),
            inverseJoinColumns = @JoinColumn(name="person_id")
    )
    private List<User> friendOf;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver")
    private List<FriendRequest> incomingFriendRequests;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    @Transactional
    public boolean isFriend(User user){
        return friends.contains(user) || friendOf.contains(user);
    }

    public void addFriend(User user){
        if(!(friends.contains(user) || friendOf.contains(user))){
            friends.add(user);
        }
    }









    //Functions not used

        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters and setters
}