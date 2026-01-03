package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.CertificateResponse;
import com.example.carespawbe.dto.Expert.CertificateUpsertRequest;
import com.example.carespawbe.service.Expert.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expert/certificates")
public class ExpertCertificateController {

    private final CertificateService certificateService;

    @GetMapping
    public List<CertificateResponse> listMyCertificates() {
        Long expertId = getCurrentExpertId();
        return certificateService.getMyCertificates(expertId);
    }

    @PostMapping
    public CertificateResponse create(@RequestBody CertificateUpsertRequest req) {
        Long expertId = getCurrentExpertId();
        return certificateService.create(expertId, req);
    }

    @PutMapping("/{id}")
    public CertificateResponse update(@PathVariable Long id, @RequestBody CertificateUpsertRequest req) {
        Long expertId = getCurrentExpertId();
        return certificateService.update(expertId, id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Long expertId = getCurrentExpertId();
        certificateService.delete(expertId, id);
    }

    private Long getCurrentExpertId() {
        return 1L;
    }
}

