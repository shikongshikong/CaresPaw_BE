package com.example.carespawbe.service.Expert;

import com.example.carespawbe.entity.Expert.CertificateEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.repository.Expert.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

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

}
