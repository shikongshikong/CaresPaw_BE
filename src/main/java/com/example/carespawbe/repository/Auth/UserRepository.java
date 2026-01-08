package com.example.carespawbe.repository.Auth;

import com.example.carespawbe.entity.Auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByEmail(String email);


    UserEntity findUserById(Long id);

    @Query("""
        select u from UserEntity u
        where
            (:q is null or :q = '' or
                lower(u.fullname) like lower(concat('%', :q, '%')) or
                lower(u.email) like lower(concat('%', :q, '%')) or
                lower(u.phoneNumber) like lower(concat('%', :q, '%'))
            )
          and (:state is null or u.state = :state)
        order by u.id desc
    """)
    List<UserEntity> adminSearch(@Param("q") String q, @Param("state") Integer state);
}
