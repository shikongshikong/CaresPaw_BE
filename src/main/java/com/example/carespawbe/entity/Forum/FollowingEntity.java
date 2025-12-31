package com.example.carespawbe.entity.Forum;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}), name = "following")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
//@Data
public class FollowingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private UserEntity followee;
}
