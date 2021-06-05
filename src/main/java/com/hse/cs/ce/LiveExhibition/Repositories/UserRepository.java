package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query(value = "SELECT u.* FROM user u WHERE u.username = ?1 and user_type = 3",
            nativeQuery = true)
    Optional<User> findVisitorByUsername(String username);
}
