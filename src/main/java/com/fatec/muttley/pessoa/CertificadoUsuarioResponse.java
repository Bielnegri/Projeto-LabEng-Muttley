package com.fatec.muttley.pessoa;

import com.fatec.muttley.certificado.Certificado;
import com.fatec.muttley.participacao.Participacao;

import java.time.LocalDate;

public record CertificadoUsuarioResponse(
        Long id,
        LocalDate dataEmissao,
        String assinatura,
        String codigoValidacao,
        String urlPublica,
        String caminhoPdf,
        Long participacaoId,
        Integer inscricao,
        String tipoParticipacao,
        EventoResumoUsuarioResponse evento
) {
    public static CertificadoUsuarioResponse from(Certificado certificado) {
        Participacao participacao = certificado.getParticipacao();
        return new CertificadoUsuarioResponse(
                certificado.getId(),
                certificado.getDataEmissao(),
                certificado.getAssinatura(),
                certificado.getCodigoValidacao(),
                certificado.getUrlPublica(),
                certificado.getCaminhoPdf(),
                participacao != null ? participacao.getId() : null,
                participacao != null ? participacao.getInscricao() : null,
                participacao != null ? participacao.getTipo() : null,
                participacao != null ? EventoResumoUsuarioResponse.from(participacao.getEvento()) : null
        );
    }
}
