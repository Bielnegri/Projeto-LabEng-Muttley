package com.fatec.muttley.participacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {
    List<Participacao> findByEventoIdOrderByInscricaoAsc(Long eventoId);

    @Query("""
            select participacao
            from Participacao participacao
            left join fetch participacao.pessoa pessoa
            left join fetch participacao.evento evento
            left join fetch evento.disciplina disciplina
            left join fetch evento.local local
            where participacao.id = :id
            """)
    Optional<Participacao> findByIdComDados(Long id);

    @Query("""
            select participacao
            from Participacao participacao
            left join fetch participacao.pessoa pessoa
            left join fetch participacao.evento evento
            left join fetch evento.disciplina disciplina
            left join fetch evento.local local
            where evento.id = :eventoId
            order by participacao.inscricao asc
            """)
    List<Participacao> findByEventoIdComDadosOrderByInscricaoAsc(Long eventoId);

    @Query("""
            select participacao
            from Participacao participacao
            left join fetch participacao.pessoa pessoa
            left join fetch participacao.evento evento
            left join fetch evento.disciplina disciplina
            left join fetch evento.local local
            where participacao.pessoa.id = :pessoaId
            order by evento.data desc, participacao.id desc
            """)
    List<Participacao> findByPessoaIdComDados(Long pessoaId);

    boolean existsByEventoIdAndPessoaId(Long eventoId, Long pessoaId);

    @Query("select coalesce(max(participacao.inscricao), 0) from Participacao participacao")
    int findMaiorNumeroInscricao();
}
