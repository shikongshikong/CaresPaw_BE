package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.CertificateResponse;
import com.example.carespawbe.dto.Expert.CertificateUpsertRequest;
import com.example.carespawbe.service.Expert.CertificateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("")
    public CertificateResponse create(@RequestBody CertificateUpsertRequest req, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        return certificateService.create(expertId, req);
    }

    @PutMapping("/{id}")
    public CertificateResponse update(@PathVariable Long id, @RequestBody CertificateUpsertRequest req, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        return certificateService.update(expertId, id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        certificateService.delete(expertId, id);
    }
}

