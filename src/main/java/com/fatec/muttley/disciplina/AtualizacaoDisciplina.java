package com.fatec.muttley.disciplina;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoDisciplina(
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotBlank(message = "O turno é obrigatório")
        String turno
) {}
