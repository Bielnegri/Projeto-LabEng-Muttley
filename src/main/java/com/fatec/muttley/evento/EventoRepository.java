package com.fatec.muttley.evento;

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
            where evento.data > :dataAtual
               or (evento.data = :dataAtual and (evento.horarioInicio is null or evento.horarioInicio >= :horaAtual))
            order by evento.data asc, evento.horarioInicio asc
            """)
    List<Evento> findProximosEventos(
            @Param("dataAtual") Date dataAtual,
            @Param("horaAtual") String horaAtual,
            Pageable pageable
    );

    @Query("""
            select e from Evento e
            where (e.data > :dataAtual
                or (e.data = :dataAtual and (e.horarioInicio is null or e.horarioInicio >= :horaAtual)))
            and (:busca = '' or lower(e.tema) like lower(concat('%', :busca, '%')))
            """)
    Page<Evento> findProximosEventosFiltrados(
            @Param("dataAtual") Date dataAtual,
            @Param("horaAtual") String horaAtual,
            @Param("busca") String busca,
            Pageable pageable
    );
}
