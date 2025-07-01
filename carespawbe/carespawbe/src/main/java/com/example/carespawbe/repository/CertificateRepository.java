package com.example.carespawbe.repository;

import com.example.carespawbe.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findAllByExpert_Id(Long expertId);
}
