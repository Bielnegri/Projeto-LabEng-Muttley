package com.fatec.muttley.email.dto;

import java.time.LocalDate;

public record CertificadoEmail(
        String destinatario,
        String nome,
        String tema,
        String dataEvento,
        String dataEmissao,
        String url
) {}
