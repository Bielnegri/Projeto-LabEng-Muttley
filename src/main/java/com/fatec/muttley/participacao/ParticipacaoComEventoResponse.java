package com.fatec.muttley.participacao;

public record ParticipacaoComEventoResponse(
        Long id,
        int inscricao,
        String tipo,
        PessoaResumoParticipacaoResponse pessoa,
        EventoResumoParticipacaoResponse evento
) {
    public static ParticipacaoComEventoResponse from(Participacao participacao) {
        return new ParticipacaoComEventoResponse(
                participacao.getId(),
                participacao.getInscricao(),
                participacao.getTipo(),
                PessoaResumoParticipacaoResponse.from(participacao.getPessoa()),
                EventoResumoParticipacaoResponse.from(participacao.getEvento())
        );
    }
}
