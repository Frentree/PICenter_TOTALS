package com.org.iopts.repository;

import com.org.iopts.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Find user by user ID (로그인 ID)
     */
    Optional<User> findByUserId(String userId);
}
