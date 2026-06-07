package com.fatec.muttley.participacao;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.enums.ModalidadeEventoEnum;
import com.fatec.muttley.evento.enums.StatusEventoEnum;

import java.time.LocalDate;

public record EventoResumoParticipacaoResponse(
        Long id,
        String tema,
        String descricao,
        LocalDate data,
        String horarioInicio,
        String horarioFim,
        ModalidadeEventoEnum modalidade,
        StatusEventoEnum status,
        String disciplina,
        String local
) {
    public static EventoResumoParticipacaoResponse from(Evento evento) {
        if (evento == null) {
            return null;
        }

        return new EventoResumoParticipacaoResponse(
                evento.getId(),
                evento.getTema(),
                evento.getDescricao(),
                evento.getData(),
                evento.getHorarioInicio(),
                evento.getHorarioFim(),
                evento.getModalidade(),
                evento.getStatus(),
                evento.getDisciplina() != null ? evento.getDisciplina().getNome() : null,
                evento.getLocal() != null ? evento.getLocal().getNome() : null
        );
    }
}
