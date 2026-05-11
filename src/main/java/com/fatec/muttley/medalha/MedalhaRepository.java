package com.fatec.muttley.medalha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MedalhaRepository extends JpaRepository<Medalha, Long> {
    @Query("""
            select pessoa.nome as participanteNome,
                   count(medalha) as total
            from Medalha medalha
            join medalha.participacao participacao
            join participacao.pessoa pessoa
            group by pessoa.id, pessoa.nome
            order by count(medalha) desc, pessoa.nome asc
            """)
    List<MedalhasPorParticipante> findTotaisPorParticipante(Pageable pageable);

    interface MedalhasPorParticipante {
        String getParticipanteNome();

        long getTotal();
    }
}
