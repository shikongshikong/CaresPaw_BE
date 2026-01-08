package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.ExpertApplyRequest;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.Expert.CertificateService;
import com.example.carespawbe.service.Expert.ExpertEntityService;
import com.example.carespawbe.service.Expert.ExpertToExpertCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expert")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpertRegisterController {

    @Autowired
    ExpertEntityService expertEntityService;

    @Autowired
    CertificateService certificateService;

    @Autowired
    ExpertToExpertCategoryService expertToCategoryService;

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBooking(@ModelAttribute ExpertApplyRequest data, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("Missing userId");
        }
        data.setUserId(userId);

        // =========================
        // ✅ 1) Upload CCCD lên Cloudinary (giống chứng chỉ)
        // =========================
        MultipartFile cccdFile = data.getIdImage();
        if (cccdFile == null || cccdFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing CCCD image");
        }

        Map<String, String> uploadedCccd = cloudinaryService.uploadImageUrlAndPublicId(
                cccdFile,
                "experts/cccd/user-" + userId
        );

        if (uploadedCccd == null || uploadedCccd.get("url") == null || uploadedCccd.get("public_id") == null) {
            return ResponseEntity.badRequest().body("Upload CCCD image failed");
        }

        // ✅ set lại vào request để service map qua entity
        // (bạn cần thêm 2 field này trong ExpertApplyRequest)
        data.setIdImageUrl(uploadedCccd.get("url"));
        data.setIdImagePublicId(uploadedCccd.get("public_id"));

        // =========================
        // 2) add expert
        // =========================
        ExpertEntity expert = expertEntityService.addExpert(data);
        if (expert == null) {
            return ResponseEntity.badRequest().body("Cannot create expert");
        }

        // =========================
        // 3) add categories
        // =========================
        List<Long> expertCategoryIds = data.getExpertCategoryIds();
        if (expertCategoryIds == null || expertCategoryIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing expertCategoryIds");
        }

        for (Long categoryId : expertCategoryIds) {
            boolean addCategory = expertToCategoryService.addExpertToCategory(expert, categoryId);
            if (!addCategory) {
                return ResponseEntity.badRequest().body("Cannot add category: " + categoryId);
            }
        }

        // =========================
        // 4) add certificate (UPLOAD CLOUDINARY)
        // =========================
        MultipartFile certFile = data.getCertificateImage();
        if (certFile == null || certFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing certificate image");
        }

        Map<String, String> uploadedCert = cloudinaryService.uploadImageUrlAndPublicId(
                certFile,
                "experts/certificates/expert-" + expert.getId()
        );

        if (uploadedCert == null || uploadedCert.get("url") == null) {
            return ResponseEntity.badRequest().body("Upload certificate image failed");
        }

        String imageUrl = uploadedCert.get("url");

        boolean saveCertificate = certificateService.addCertificate(expert, imageUrl);
        if (!saveCertificate) {
            return ResponseEntity.badRequest().body("Save certificate failed");
        }

        return ResponseEntity.ok().build();
    }
}
