package com.fatec.muttley.email.dto;

public record CredenciaisEmail(
        String destinatario,
        String nome,
        String token,
        String baseUrl
) {}
