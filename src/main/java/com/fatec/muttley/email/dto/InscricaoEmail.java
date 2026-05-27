package com.fatec.muttley.email.dto;

public record InscricaoEmail(
        String destinatario,
        String nome,
        String tema,
        String data,
        String horarioInicio,
        String horarioFim,
        String local,
        int inscricao
) {}