package com.bee.cookwithfriends.repositories;

import com.bee.cookwithfriends.entity.FriendRequest;
import com.bee.cookwithfriends.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT DISTINCT fr.sender FROM FriendRequest fr WHERE fr.receiver.id = :receiverId")
    List<User> findSendersByReceiverId(@Param("receiverId") int receiverId);
}
