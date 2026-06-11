package com.fatec.muttley.email.dto;

public record CadastroEmail(
        String destinatario,
        String nome,
        String id,
        String baseUrl
) {}
