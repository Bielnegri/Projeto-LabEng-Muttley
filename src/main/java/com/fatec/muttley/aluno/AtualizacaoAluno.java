package com.fatec.muttley.aluno;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoAluno(
        Long id,
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "Instituição é obrigatório")
        String instituicao,

        @NotBlank(message = "Matrícula é obrigatório")
        String matricula
) {}