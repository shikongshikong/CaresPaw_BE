package com.example.carespawbe.service.Admin;

import com.example.carespawbe.dto.Admin.AdminExpertVerifyItemResponse;
import com.example.carespawbe.dto.Expert.CertificateResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.CertificateEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.entity.Expert.ExpertToExpertCategoryEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Expert.CertificateRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import com.example.carespawbe.repository.Expert.ExpertToExpertCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminExpertVerifyService {

    private final ExpertRepository expertRepository;
    private final CertificateRepository certificateRepository;
    private final ExpertToExpertCategoryRepository expertToExpertCategoryRepository;
    private final UserRepository userRepository;

    public static final int PENDING = 0;
    public static final int ACTIVE  = 1;
    public static final int BLOCK   = 2;

    @Transactional
    public List<AdminExpertVerifyItemResponse> listPending() {
        return expertRepository.findByStatusOrderByIdDesc(PENDING)
                .stream()
                .map(this::toItem)
                .toList();
    }

    @Transactional
    public List<AdminExpertVerifyItemResponse> list(Integer status) {
        List<ExpertEntity> experts;

        if (status == null) {
            // ✅ Tất cả trạng thái
            experts = expertRepository.findAllByOrderByIdDesc();
        } else {
            // ✅ Lọc theo trạng thái
            experts = expertRepository.findByStatusOrderByIdDesc(status);
        }

        return experts.stream()
                .map(this::toItem)
                .toList();
    }

    @Transactional
    public void approve(Long expertId) {
        ExpertEntity expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        expert.setStatus(ACTIVE);
        expertRepository.save(expert);

        UserEntity user = expert.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for this expert");
        }

        if (user.getRole() == 0) return;
        user.setRole(3);
        userRepository.save(user);
    }

    @Transactional
    public void reject(Long expertId) {
        ExpertEntity expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));
        expert.setStatus(BLOCK); // hoặc tạo status REJECTED riêng
        expertRepository.save(expert);
    }

    private AdminExpertVerifyItemResponse toItem(ExpertEntity e) {

        // ✅ lấy tất cả chứng chỉ mà user upload
        List<CertificateResponse> certs = certificateRepository
                .findByExpert_IdOrderByIdDesc(e.getId())
                .stream()
                .map(this::toCertificateResponse)
                .toList();

        // ✅ lấy chuyên môn (category) mà user chọn
        List<String> categories = expertToExpertCategoryRepository
                .findByExpert_Id(e.getId())
                .stream()
                .map(ExpertToExpertCategoryEntity::getExpertCategory) // <- field đúng
                .filter(cat -> cat != null && cat.getName() != null)
                .map(cat -> cat.getName())
                .distinct()
                .toList();

        return new AdminExpertVerifyItemResponse(
                e.getId(),
                e.getUser() != null ? e.getUser().getId() : null,
                e.getFullName(),
                e.getIdImage(),
                e.getCreatedAt(),
                e.getStatus(),
                categories,
                certs
        );
    }

    private CertificateResponse toCertificateResponse(CertificateEntity c) {
        return new CertificateResponse(
                c.getId(),
                c.getName(),
                c.getCredentialID(),
                c.getIssue_place(),
                c.getIssue_date(),
                c.getExpiry_date(),
                c.getImage(),
                c.getStatus()
        );
    }
}
