package com.example.carespawbe.repository;

import com.example.carespawbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);


    User findUserById(Long id);
}
