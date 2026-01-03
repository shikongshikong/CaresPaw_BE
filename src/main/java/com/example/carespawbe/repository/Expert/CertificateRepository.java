package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository  extends JpaRepository<CertificateEntity, Long> {
    List<CertificateEntity> findByExpert_IdOrderByIdDesc(Long expertId);

    List<CertificateEntity> findByExpert_IdAndStatusOrderByIdDesc(Long expertId, Integer status);

    Optional<CertificateEntity> findByIdAndExpert_Id(Long id, Long expertId);
}
