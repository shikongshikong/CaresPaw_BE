package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.ExpertApplyRequest;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.service.Expert.CertificateService;
import com.example.carespawbe.service.Expert.ExpertEntityService;
import com.example.carespawbe.service.Expert.ExpertToExpertCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expert")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpertRegisterController {

    @Autowired
    ExpertEntityService  expertEntityService;

    @Autowired
    CertificateService certificateService;

    @Autowired
    ExpertToExpertCategoryService  expertToCategoryService;

    // @ModelAttribute: nhận dạng file
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBooking(@ModelAttribute ExpertApplyRequest data, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        data.setUserId(userId);

        // add expert
        // need to process image : save to cloud -> get name/url and save to db
        ExpertEntity expert = expertEntityService.addExpert(data);
        if (expert == null) {
            return ResponseEntity.badRequest().build();
        }

        // add categories
        List<Long> expertCategoryIds = data.getExpertCategoryIds();
        if (expertCategoryIds == null) {
            return ResponseEntity.badRequest().build();
        }
        for (Long categoryId : expertCategoryIds) {
            boolean addCategory = expertToCategoryService.addExpertToCategory(expert, categoryId);
            if  (!addCategory) {
                return ResponseEntity.badRequest().build();
            }
        }

        // add certificate
        boolean saveCertificate = certificateService.addCertificate(expert, data.getCertificateImage().getOriginalFilename());
        if (!saveCertificate) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
