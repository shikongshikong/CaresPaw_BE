package com.example.carespawbe.repository.Auth;

import com.example.carespawbe.entity.Auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByEmail(String email);


    UserEntity findUserById(Long id);
}
