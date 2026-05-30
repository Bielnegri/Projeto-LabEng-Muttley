package com.fatec.muttley.evento;

import com.fatec.muttley.evento.enums.ModalidadeEventoEnum;
import com.fatec.muttley.evento.enums.StatusEventoEnum;

import java.time.LocalDate;

public record EventoPublicoResponse(
        long id,
        String tema,
        String descricao,
        LocalDate data,
        String horarioInicio,
        String horarioFim,
        ModalidadeEventoEnum modalidade,
        StatusEventoEnum status,
        String disciplina,
        String local,
        boolean inscricoesEncerradas
) {
    public static EventoPublicoResponse from(Evento evento, boolean inscricoesEncerradas) {
        return new EventoPublicoResponse(
                evento.getId(),
                evento.getTema(),
                evento.getDescricao(),
                evento.getData(),
                evento.getHorarioInicio(),
                evento.getHorarioFim(),
                evento.getModalidade(),
                evento.getStatus(),
                evento.getDisciplina() != null ? evento.getDisciplina().getNome() : null,
                evento.getLocal() != null ? evento.getLocal().getNome() : null,
                inscricoesEncerradas
        );
    }
}
