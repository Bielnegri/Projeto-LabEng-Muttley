package com.fatec.muttley.email.dto;

public record CertificadoEmail(
        String destinatario,
        String nome,
        String tema,
        String dataEvento,
        String dataEmissao,
        String baseUrl,
        String urlCert
) {}
