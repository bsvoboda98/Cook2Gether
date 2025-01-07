package com.bee.cookwithfriends.repositories;

import com.bee.cookwithfriends.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String username);
    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
