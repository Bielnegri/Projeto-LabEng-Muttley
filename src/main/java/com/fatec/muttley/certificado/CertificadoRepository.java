package com.fatec.muttley.certificado;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

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

    @Query("""
            select evento.id as eventoId,
                   evento.tema as eventoTema,
                   count(certificado) as total
            from Certificado certificado
            join certificado.participacao participacao
            join participacao.evento evento
            group by evento.id, evento.tema
            order by count(certificado) desc, evento.tema asc
            """)
    List<CertificadosPorEvento> findTotaisPorEvento(Pageable pageable);

    boolean existsByCodigoValidacao(String codigoValidacao);
    boolean existsByParticipacaoId(Long participacaoId);

    long countByDataEmissaoGreaterThanEqual(Date dataInicio);

    long countByDataEmissaoGreaterThanEqualAndDataEmissaoLessThan(Date dataInicio, Date dataFim);

    @Query("""
            select certificado
            from Certificado certificado
            left join fetch certificado.participacao participacao
            left join fetch participacao.pessoa pessoa
            left join fetch participacao.evento evento
            left join fetch evento.disciplina disciplina
            where certificado.codigoValidacao = :codigoValidacao
            """)
    Optional<Certificado> findByCodigoValidacaoComDados(String codigoValidacao);

    interface CertificadosPorEvento {
        Long getEventoId();

        String getEventoTema();

        long getTotal();
    }
}
