package com.fatec.muttley.pessoa;

import com.fatec.muttley.medalha.Medalha;
import com.fatec.muttley.participacao.Participacao;

public record MedalhaUsuarioResponse(
        Long id,
        String nome,
        String descricao,
        Long participacaoId,
        Integer inscricao,
        String tipoParticipacao,
        EventoResumoUsuarioResponse evento
) {
    public static MedalhaUsuarioResponse from(Medalha medalha) {
        Participacao participacao = medalha.getParticipacao();
        return new MedalhaUsuarioResponse(
                medalha.getId(),
                medalha.getNome(),
                medalha.getDescricao(),
                participacao != null ? participacao.getId() : null,
                participacao != null ? participacao.getInscricao() : null,
                participacao != null ? participacao.getTipo() : null,
                participacao != null ? EventoResumoUsuarioResponse.from(participacao.getEvento()) : null
        );
    }
}
