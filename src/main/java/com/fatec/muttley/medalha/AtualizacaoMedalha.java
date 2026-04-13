package com.fatec.muttley.medalha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoMedalha (
        Long id,
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Marca é obrigatória")
        Long alunoId
) {}

