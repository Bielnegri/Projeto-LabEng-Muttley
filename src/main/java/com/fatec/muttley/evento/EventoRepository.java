package com.fatec.muttley.evento;

import com.fatec.muttley.evento.enums.StatusEventoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    @Query("""
            select evento
            from Evento evento
            where evento.status in :statuses
            order by evento.data asc, evento.horarioInicio asc
            """)
    List<Evento> findProximosEventos(
            @Param("statuses") List<StatusEventoEnum> statuses,
            Pageable pageable
    );

    @Query("""
            select evento
            from Evento evento
            left join fetch evento.disciplina
            left join fetch evento.local
            where evento.status = :status
            order by evento.data desc, evento.horarioFim desc
            """)
    List<Evento> findEventosEncerrados(
            @Param("status") StatusEventoEnum status,
            Pageable pageable
    );

    @Query("""
            select e from Evento e
            where e.status in :statuses
            and (:busca = '' or lower(e.tema) like lower(concat('%', :busca, '%')))
            """)
    Page<Evento> findProximosEventosFiltrados(
            @Param("statuses") List<StatusEventoEnum> statuses,
            @Param("busca") String busca,
            Pageable pageable
    );
}
