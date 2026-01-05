package com.example.carespawbe.service.Expert.qanda;

import com.example.carespawbe.dto.Expert.qanda.ExpertCardResponse;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QandAService {

    private final AppointmentRepository appointmentRepository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;

    public List<ExpertCardResponse> getBookedExperts(Long userId) {
//        if (principal == null || principal.getName() == null) {
//            throw new RuntimeException("Unauthenticated");
//        }

        // default: principal.getName() is email
//        UserEntity user = userRepository.findByEmail(principal.getName())
//                .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));

        List<ExpertEntity> experts = expertRepository.findDistinctExpertsBookedByUserId(userId);
        return experts.stream().map(this::toCard).toList();
    }

    public Page<ExpertCardResponse> searchExperts(
            String q, Double minPrice, Double maxPrice, String sort, int page, int size
    ) {
        Specification<ExpertEntity> spec = (root, query, cb) -> cb.conjunction();

        // only active experts
        spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), 1));

        if (q != null && !q.trim().isEmpty()) {
            String like = "%" + q.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("fullName")), like),
                            cb.like(cb.lower(root.get("biography")), like),
                            cb.like(cb.lower(root.get("location")), like)
                    )
            );
        }

        // filter by price (Double)
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        Sort s = switch (sort == null ? "" : sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "exp_desc" -> Sort.by("experienceYear").descending();
            default -> Sort.by("id").descending(); // newest
        };

        Pageable pageable = PageRequest.of(page, size, s);
        return expertRepository.findAll(spec, pageable).map(this::toCard);
    }

    private ExpertCardResponse toCard(ExpertEntity e) {
        return ExpertCardResponse.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .idImage(e.getIdImage())
                .location(e.getLocation())
                .experienceYear(e.getExperienceYear())
                .status(e.getStatus())
                .price(e.getPrice()) // ONLY ONE
                .portfolioLink(e.getPortfolioLink())
                .build();
    }
}



