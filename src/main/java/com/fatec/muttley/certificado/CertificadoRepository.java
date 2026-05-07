package com.fatec.muttley.certificado;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    @Query("""
            select certificado
            from Certificado certificado
            left join fetch certificado.participacao participacao
            left join fetch participacao.pessoa pessoa
            left join fetch participacao.evento evento
            order by certificado.dataEmissao desc, certificado.id desc
            """)
    List<Certificado> findUltimosEmitidos(Pageable pageable);
}
