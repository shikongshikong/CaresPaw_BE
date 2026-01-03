package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.ExpertApplyRequest;
import com.example.carespawbe.dto.Expert.ExpertCVResponse;
import com.example.carespawbe.dto.Expert.ExpertCVUpdateRequest;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.CertificateEntity;
import com.example.carespawbe.entity.Expert.ExpertCategoryEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.entity.Expert.ExpertToExpertCategoryEntity;
import com.example.carespawbe.mapper.Expert.ExpertEntityMapper;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Expert.ExpertCategoryRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertEntityService {

    @Autowired
    ExpertEntityMapper expertEntityMapper;

    @Autowired
    ExpertRepository  expertRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private ExpertCategoryRepository categoryRepository;

    public ExpertEntity addExpert(ExpertApplyRequest applyRequest) {
        ExpertEntity expertEntity = expertEntityMapper.toExpertEntity(applyRequest);
        // just save file name
        expertEntity.setIdImage(applyRequest.getIdImage().getOriginalFilename());

        try {
            return expertRepository.save(expertEntity);
        }  catch (Exception e) {
            System.out.println("Save Entity Error!: " + e.getMessage());
            return null;
        }
    }

    public ExpertEntity getExpertById(Long expertId) {
        return expertRepository.findById(expertId).orElse(null);
    }

    public ExpertEntity getExpertByUserId(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null) return null;
        return expertRepository.findByUser(userEntity);
    }

    public ExpertCVResponse getExpertCv(Long expertId) {
        ExpertEntity e = expertRepository.findByExpertIdWithCvData(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found for expertId=" + expertId));

        // categoryIds
        List<Long> categoryIds = (e.getExpertToCategoryEntities() == null) ? List.of() :
                e.getExpertToCategoryEntities().stream()
                        .map(x -> x.getExpertCategory().getId())   // <-- tùy id type
                        .distinct()
                        .sorted(Comparator.comparingLong(Long::longValue))
                        .toList();

        // certificates -> degrees
        List<ExpertCVResponse.CertificateItem> degrees = (e.getCertificateEntities() == null) ? List.of() :
                e.getCertificateEntities().stream()
                        .sorted(Comparator.comparing(CertificateEntity::getIssue_date,
                                Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                        .map(cert -> ExpertCVResponse.CertificateItem.builder()
                                .id(cert.getId())
                                .title(cert.getName())
                                .issuer(cert.getIssue_place())
                                .year(cert.getIssue_date() != null ? cert.getIssue_date().getYear() : null)
                                .imageUrl(cert.getImage())
                                .status(cert.getStatus()) // 0/1/2
                                .build())
                        .toList();

        // phone/avatar nếu UserEntity có field tương ứng:
        // giả sử user có getPhone() và getAvatarUrl()
        String phone = null;
        String avatarUrl = null;
        if (e.getUser() != null) {
            try {
                phone = (String) e.getUser().getClass().getMethod("getPhoneNumber").invoke(e.getUser());
            } catch (Exception ignore) {}
            try {
                avatarUrl = (String) e.getUser().getClass().getMethod("getAvatar").invoke(e.getUser());
            } catch (Exception ignore) {}
        }

        return ExpertCVResponse.builder()
                .fullName(e.getFullName())
                .biography(e.getBiography())
                .yearsOfExperience(e.getExperienceYear())
                .cccdImageUrl(e.getIdImage())
                .address(e.getLocation())
                .sessionPrice(e.getSessionPrice())
                .phone(phone)
                .avatarUrl(avatarUrl)
                .categoryIds(categoryIds)
                .degrees(degrees)
                .build();
    }

    // update cv
    private static final int CERT_PENDING = 0;

    @Transactional
    public ExpertCVResponse updateExpertCv(Long expertId, ExpertCVUpdateRequest req) {
        ExpertEntity expert = expertRepository.findByExpertIdWithCvData(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found for userId=" + expertId));

        // 1) Update scalar fields (no validation)
        expert.setBiography(req.getBiography());
        expert.setLocation(req.getLocation());
        expert.setSessionPrice(req.getSessionPrice());
        expert.setPortfolioLink(req.getPortfolioLink());

        // 2) Sync categories (no existence validation)
        syncCategoriesNoValidate(expert, req.getCategoryIds());

        // 3) Sync certificates (no field validation, but enforce ownership)
        syncCertificatesNoValidate(expert, req.getDegrees());

        ExpertEntity saved = expertRepository.save(expert);
        return mapToResponse(saved);
    }

    private void syncCategoriesNoValidate(ExpertEntity expert, List<Long> categoryIds) {
        if (categoryIds == null) categoryIds = List.of();

        // load any existing ones by id (if some ids not exist -> they will be missing)
        List<ExpertCategoryEntity> categories = categoryRepository.findAllById(categoryIds);

        // rebuild join entities from found categories
        List<ExpertToExpertCategoryEntity> newLinks = categories.stream()
                .map(cat -> {
                    ExpertToExpertCategoryEntity link = new ExpertToExpertCategoryEntity();
                    link.setExpert(expert);
                    link.setExpertCategory(cat);
                    return link;
                })
                .toList();

        // replace list (orphanRemoval true)
        if (expert.getExpertToCategoryEntities() == null) {
            expert.setExpertToCategoryEntities(new ArrayList<>());
        }
        expert.getExpertToCategoryEntities().clear();
        expert.getExpertToCategoryEntities().addAll(newLinks);
    }

    private void syncCertificatesNoValidate(ExpertEntity expert, List<ExpertCVUpdateRequest.CertificateUpsertItem> incoming) {
        if (incoming == null) incoming = List.of();

        if (expert.getCertificateEntities() == null) {
            expert.setCertificateEntities(new ArrayList<>());
        }

        Map<Long, CertificateEntity> existingById = expert.getCertificateEntities().stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(CertificateEntity::getId, Function.identity()));

        Set<Long> keepIds = new HashSet<>();

        for (ExpertCVUpdateRequest.CertificateUpsertItem item : incoming) {
            if (item.getId() != null) {
                CertificateEntity ex = existingById.get(item.getId());
                if (ex == null) {
                    // security/ownership: don't allow updating someone else's cert
                    throw new RuntimeException("Certificate not found or not belong to expert: " + item.getId());
                }

                ex.setName(item.getTitle());
                ex.setIssue_place(item.getIssuer());
                ex.setImage(item.getImageUrl());

                if (item.getYear() != null) {
                    ex.setIssue_date(LocalDate.of(item.getYear(), 1, 1));
                } else {
                    ex.setIssue_date(null);
                }

                keepIds.add(ex.getId());
            } else {
                CertificateEntity n = new CertificateEntity();
                n.setExpert(expert);
                n.setName(item.getTitle());
                n.setIssue_place(item.getIssuer());
                n.setImage(item.getImageUrl());
                n.setStatus(CERT_PENDING); // new always pending

                if (item.getYear() != null) {
                    n.setIssue_date(LocalDate.of(item.getYear(), 1, 1));
                }

                expert.getCertificateEntities().add(n);
            }
        }

        // delete removed ones (orphanRemoval true)
        expert.getCertificateEntities().removeIf(c ->
                c.getId() != null && !keepIds.contains(c.getId())
        );
    }

    private ExpertCVResponse mapToResponse(ExpertEntity e) {
        List<Long> categoryIds = (e.getExpertToCategoryEntities() == null) ? List.of() :
                e.getExpertToCategoryEntities().stream()
                        .map(x -> x.getExpertCategory().getId())
                        .distinct()
                        .sorted()
                        .toList();

        List<ExpertCVResponse.CertificateItem> degrees = (e.getCertificateEntities() == null) ? List.of() :
                e.getCertificateEntities().stream()
                        .map(cert -> ExpertCVResponse.CertificateItem.builder()
                                .id(cert.getId())
                                .title(cert.getName())
                                .issuer(cert.getIssue_place())
                                .year(cert.getIssue_date() != null ? cert.getIssue_date().getYear() : null)
                                .imageUrl(cert.getImage())
                                .status(cert.getStatus())
                                .build())
                        .toList();

        return ExpertCVResponse.builder()
                .fullName(e.getFullName())
                .biography(e.getBiography())
                .yearsOfExperience(e.getExperienceYear())
                .cccdImageUrl(e.getIdImage())
                .address(e.getLocation())
                .sessionPrice(e.getSessionPrice())
                .categoryIds(categoryIds)
                .portfolioLink(e.getPortfolioLink())
                .degrees(degrees)
                .build();
    }
}
