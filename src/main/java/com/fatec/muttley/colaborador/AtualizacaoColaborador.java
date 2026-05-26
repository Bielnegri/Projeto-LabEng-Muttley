package com.fatec.muttley.colaborador;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoColaborador(
        Long id,

        @NotBlank(message = "Função é obrigatória")
        String funcao,

        @NotBlank(message = "Disponibilidade é obrigatória")
        String disponibilidade,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo
) {

}
