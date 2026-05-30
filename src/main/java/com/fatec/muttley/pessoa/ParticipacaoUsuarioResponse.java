package com.fatec.muttley.pessoa;

import com.fatec.muttley.participacao.Participacao;

public record ParticipacaoUsuarioResponse(
        Long id,
        int inscricao,
        String tipo,
        EventoResumoUsuarioResponse evento
) {
    public static ParticipacaoUsuarioResponse from(Participacao participacao) {
        return new ParticipacaoUsuarioResponse(
                participacao.getId(),
                participacao.getInscricao(),
                participacao.getTipo(),
                EventoResumoUsuarioResponse.from(participacao.getEvento())
        );
    }
}
