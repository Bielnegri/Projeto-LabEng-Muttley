package com.fatec.muttley.pessoa;

import com.fatec.muttley.medalha.Medalha;
import com.fatec.muttley.medalha.TipoMedalha;
import com.fatec.muttley.participacao.Participacao;

public record MedalhaUsuarioResponse(
        Long id,
        String nome,
        String descricao,
        TipoMedalha tipo,
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
                medalha.getTipo(),
                participacao != null ? participacao.getId() : null,
                participacao != null ? participacao.getInscricao() : null,
                participacao != null ? participacao.getTipo() : null,
                participacao != null ? EventoResumoUsuarioResponse.from(participacao.getEvento()) : null
        );
    }
}
