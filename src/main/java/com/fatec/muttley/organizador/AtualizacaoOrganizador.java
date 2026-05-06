package com.fatec.muttley.organizador;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoOrganizador(
        Long id,

        @NotBlank(message = "Instituição é obrigatória")
        String instituicao,

        @NotBlank(message = "Cargo é obrigatório")
        String cargo
) {

}
