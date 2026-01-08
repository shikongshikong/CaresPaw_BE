package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.CertificateResponse;
import com.example.carespawbe.dto.Expert.CertificateUpsertRequest;
import com.example.carespawbe.service.Expert.CertificateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expert/certificates")
public class ExpertCertificateController {

    private final CertificateService certificateService;

    @GetMapping("")
    public List<CertificateResponse> listMyCertificates(HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        return certificateService.getMyCertificates(expertId);
    }

    // ✅ Create: multipart/form-data (data + image)
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CertificateResponse create(
            @RequestPart("data") CertificateUpsertRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest request
    ) {
        Long expertId = (Long) request.getAttribute("expertId");
        return certificateService.create(expertId, req, image);
    }

    // ✅ Update: multipart/form-data (data + image)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CertificateResponse update(
            @PathVariable Long id,
            @RequestPart("data") CertificateUpsertRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest request
    ) {
        Long expertId = (Long) request.getAttribute("expertId");
        return certificateService.update(expertId, id, req, image);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        certificateService.delete(expertId, id);
    }
}
