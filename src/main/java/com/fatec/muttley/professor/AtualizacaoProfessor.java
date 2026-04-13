package com.fatec.muttley.professor;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoProfessor (
        Long id,
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Formação é obrigatório")
        String formacao
) {}
