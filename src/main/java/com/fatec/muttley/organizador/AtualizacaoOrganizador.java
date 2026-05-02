package com.fatec.muttley.organizador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoOrganizador(
        Long id,

        @NotNull(message = "Instituição é obrigatória")
        String instituicao,

        @NotBlank(message = "Cargo é obrigatório")
        String cargo
) {

}
