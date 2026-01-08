package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.CertificateResponse;
import com.example.carespawbe.dto.Expert.CertificateUpsertRequest;
import com.example.carespawbe.entity.Expert.CertificateEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.repository.Expert.CertificateRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import com.example.carespawbe.service.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final ExpertRepository expertRepository;
    private final CloudinaryService cloudinaryService;

    public static final int PENDING = 0;
    public static final int APPROVED = 1;
    public static final int REJECTED = 2;

    // ===== Expert side =====

    public List<CertificateResponse> getMyCertificates(Long expertId) {
        return certificateRepository.findByExpert_IdOrderByIdDesc(expertId)
                .stream().map(this::toResponse).toList();
    }

    /**
     * Create certificate (optionally upload image)
     */
    @Transactional
    public CertificateResponse create(Long expertId, CertificateUpsertRequest req, MultipartFile imageFile) {
        ExpertEntity expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        CertificateEntity c = new CertificateEntity();
        c.setExpert(expert);

        applyUpsert(c, req);

        // Upload image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, String> uploaded = cloudinaryService.uploadImageUrlAndPublicId(
                    imageFile,
                    "experts/certificates/expert-" + expertId
            );

            if (uploaded == null || uploaded.get("url") == null || uploaded.get("public_id") == null) {
                throw new RuntimeException("Upload certificate image failed");
            }

            c.setImage(uploaded.get("url"));
            // cần field imagePublicId trong entity
            c.setImagePublicId(uploaded.get("public_id"));
        }

        // moderation rule: create => pending
        c.setStatus(PENDING);

        return toResponse(certificateRepository.save(c));
    }

    /**
     * Update certificate (optionally upload new image)
     * Rule: edit => pending again
     */
    @Transactional
    public CertificateResponse update(Long expertId, Long certId, CertificateUpsertRequest req, MultipartFile imageFile) {
        CertificateEntity c = certificateRepository.findByIdAndExpert_Id(certId, expertId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        applyUpsert(c, req);

        // If has new image -> upload and replace
        if (imageFile != null && !imageFile.isEmpty()) {
            String oldPublicId = c.getImagePublicId();

            Map<String, String> uploaded = cloudinaryService.uploadImageUrlAndPublicId(
                    imageFile,
                    "experts/certificates/expert-" + expertId
            );

            if (uploaded == null || uploaded.get("url") == null || uploaded.get("public_id") == null) {
                throw new RuntimeException("Upload certificate image failed");
            }

            c.setImage(uploaded.get("url"));
            c.setImagePublicId(uploaded.get("public_id"));

            // delete old image after upload success
            if (oldPublicId != null && !oldPublicId.isBlank()) {
                cloudinaryService.deleteImage(oldPublicId);
            }
        }

        // moderation rule: edit => pending again
        c.setStatus(PENDING);

        return toResponse(certificateRepository.save(c));
    }

    @Transactional
    public void delete(Long expertId, Long certId) {
        CertificateEntity c = certificateRepository.findByIdAndExpert_Id(certId, expertId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        // delete image on cloudinary if have publicId
        String publicId = c.getImagePublicId();
        if (publicId != null && !publicId.isBlank()) {
            cloudinaryService.deleteImage(publicId);
        }

        certificateRepository.delete(c);
    }

    public boolean addCertificate(ExpertEntity expert, String imageUrl) {
        Integer status = PENDING; // 0: pending
        CertificateEntity certificateEntity = new CertificateEntity(expert, imageUrl, status);
        try {
            certificateRepository.save(certificateEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("Error adding certificate: " + ex.getMessage());
            return false;
        }
    }

    // ===== Admin side =====

    @Transactional
    public CertificateResponse approve(Long certId) {
        CertificateEntity c = certificateRepository.findById(certId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        c.setStatus(APPROVED);
        return toResponse(certificateRepository.save(c));
    }

    @Transactional
    public CertificateResponse reject(Long certId, String adminNote) {
        CertificateEntity c = certificateRepository.findById(certId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        c.setStatus(REJECTED);
        // nếu bạn có adminNote thì set ở đây
        return toResponse(certificateRepository.save(c));
    }

    // ===== Public side =====

    public List<CertificateResponse> getApprovedCertificatesOfExpert(Long expertId) {
        return certificateRepository.findByExpert_IdAndStatusOrderByIdDesc(expertId, APPROVED)
                .stream().map(this::toResponse).toList();
    }

    // ===== helpers =====

    private void applyUpsert(CertificateEntity c, CertificateUpsertRequest req) {
        c.setName(req.getName());
        c.setCredentialID(req.getCredentialID());
        c.setIssue_place(req.getIssue_place());
        c.setIssue_date(req.getIssue_date());
        c.setExpiry_date(req.getExpiry_date());

        // NOTE:
        // - Không setImage từ req nữa vì ảnh sẽ lấy từ MultipartFile upload lên Cloudinary
        // - Nếu bạn muốn cho phép update bằng URL: uncomment và ưu tiên logic theo nhu cầu
        // c.setImage(req.getImage());
    }

    private CertificateResponse toResponse(CertificateEntity c) {
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
