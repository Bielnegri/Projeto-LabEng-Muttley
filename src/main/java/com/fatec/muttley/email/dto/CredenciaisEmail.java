package com.fatec.muttley.email.dto;

public record CredenciaisEmail(
        String destinatario,
        String nome,
        String user,
        String senha
) {}
