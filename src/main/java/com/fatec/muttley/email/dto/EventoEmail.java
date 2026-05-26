package com.fatec.muttley.email.dto;

public record EventoEmail(
        String destinatario,
        String nome,
        String tema,
        String data
) {}
