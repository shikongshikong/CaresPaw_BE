package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.CertificateResponse;
import com.example.carespawbe.dto.Expert.CertificateUpsertRequest;
import com.example.carespawbe.entity.Expert.CertificateEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.repository.Expert.CertificateRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    ExpertRepository expertRepository;

    public boolean addCertificate(ExpertEntity expert, String image){
        Integer status = 0; // status 0: pending
        CertificateEntity certificateEntity = new CertificateEntity(expert, image, status);
        try {
            certificateRepository.save(certificateEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("Error adding certificate: " + ex.getMessage());
            return false;
        }
    }

    public static final int PENDING = 0;
    public static final int APPROVED = 1;
    public static final int REJECTED = 2;

    // ===== Expert side =====

    public List<CertificateResponse> getMyCertificates(Long expertId) {
        return certificateRepository.findByExpert_IdOrderByIdDesc(expertId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public CertificateResponse create(Long expertId, CertificateUpsertRequest req) {
        ExpertEntity expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        CertificateEntity c = new CertificateEntity();
        c.setExpert(expert);
        applyUpsert(c, req);

        // moderation rule: create => pending
        c.setStatus(PENDING);
//        c.setAdminNote(null);

        return toResponse(certificateRepository.save(c));
    }

    @Transactional
    public CertificateResponse update(Long expertId, Long certId, CertificateUpsertRequest req) {
        CertificateEntity c = certificateRepository.findByIdAndExpert_Id(certId, expertId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        applyUpsert(c, req);

        // moderation rule: edit => pending again
        c.setStatus(PENDING);
//        c.setAdminNote(null);

        return toResponse(certificateRepository.save(c));
    }

    @Transactional
    public void delete(Long expertId, Long certId) {
        CertificateEntity c = certificateRepository.findByIdAndExpert_Id(certId, expertId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        certificateRepository.delete(c);
    }

    // ===== Admin side =====

    @Transactional
    public CertificateResponse approve(Long certId) {
        CertificateEntity c = certificateRepository.findById(certId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        c.setStatus(APPROVED);
//        c.setAdminNote(null);
        return toResponse(certificateRepository.save(c));
    }

    @Transactional
    public CertificateResponse reject(Long certId, String adminNote) {
        CertificateEntity c = certificateRepository.findById(certId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        c.setStatus(REJECTED);
//        c.setAdminNote(adminNote);
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
        c.setImage(req.getImage());
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
//                c.getAdminNote()
        );
    }


}
