package com.fatec.muttley.certificado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    
}
