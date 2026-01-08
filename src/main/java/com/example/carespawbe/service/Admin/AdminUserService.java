package com.example.carespawbe.service.Admin;

import com.example.carespawbe.dto.Admin.AdminUserItemResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    /**
     * List users for admin table (only fields in UI)
     * - q: search by email or phone
     * - state: 1=Active, 0=Banned (null = all)
     */
    public List<AdminUserItemResponse> listUsers(String q, Integer state) {
        String keyword = (q == null) ? null : q.trim().toLowerCase();

        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .filter(u -> keyword == null || keyword.isBlank()
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(keyword))
                        || (u.getPhoneNumber() != null && u.getPhoneNumber().toLowerCase().contains(keyword)))
                .filter(u -> state == null || u.getState() == state)
                .map(this::toResponse)
                .toList();
    }

    /**
     * Update state: 1=Active, 0=Banned
     * - block changing state for Admin (role=0)
     */
    @Transactional
    public void updateState(Long id, Integer value) {
        if (value == null || (value != 0 && value != 1)) {
            throw new IllegalArgumentException("state value must be 0 (Banned) or 1 (Active)");
        }

        UserEntity u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        // không cho ban admin
        if (u.getRole() == 0) {
            throw new RuntimeException("Cannot change state of Admin user");
        }

        u.setState(value);
        userRepository.save(u);
    }

    private AdminUserItemResponse toResponse(UserEntity u) {
        return new AdminUserItemResponse(
                u.getId(),
                u.getFullname(),
                u.getEmail(),
                mapRole(u.getRole()),
                mapState(u.getState())
        );
    }

    private String mapRole(int role) {
        return switch (role) {
            case 0 -> "Admin";
            case 2 -> "Shop Owner";
            case 3 -> "Expert (Vet)";
            default -> "Member"; // role = 1
        };
    }

    private String mapState(int state) {
        return state == 1 ? "Active" : "Banned";
    }
}
