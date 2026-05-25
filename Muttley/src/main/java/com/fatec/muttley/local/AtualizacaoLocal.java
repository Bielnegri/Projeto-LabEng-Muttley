package com.fatec.muttley.local;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoLocal(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Capacidade é obrigatória")
        int capacidade,

        @NotNull(message = "Endereço é obrigatório")
        Long enderecoId
) {

}
