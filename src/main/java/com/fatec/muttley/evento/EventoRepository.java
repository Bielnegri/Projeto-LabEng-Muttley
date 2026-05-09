package com.fatec.muttley.evento;

import com.fatec.muttley.evento.enums.StatusEventoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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
            and (:statusFiltro is null or e.status = :statusFiltro)
            and (:busca = '' or lower(e.tema) like lower(concat('%', :busca, '%')))
            """)
    Page<Evento> findProximosEventosFiltrados(
            @Param("statuses") List<StatusEventoEnum> statuses,
            @Param("statusFiltro") StatusEventoEnum statusFiltro,
            @Param("busca") String busca,
            Pageable pageable
    );

    long countByStatusIn(List<StatusEventoEnum> statuses);

    @Query("""
            select count(evento)
            from Evento evento
            where evento.status in :statuses
            and evento.data between :inicio and :fim
            """)
    long countEventosAtivosNoPeriodo(
            @Param("statuses") List<StatusEventoEnum> statuses,
            @Param("inicio") Date inicio,
            @Param("fim") Date fim
    );
}
