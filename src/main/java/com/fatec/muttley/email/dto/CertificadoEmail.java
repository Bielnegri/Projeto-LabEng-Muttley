package com.fatec.muttley.email.dto;

import java.time.LocalDate;

public record CertificadoEmail(
        String destinatario,
        String nome,
        String tema,
        LocalDate dataEvento,
        LocalDate dataEmissao,
        String url
) {}
