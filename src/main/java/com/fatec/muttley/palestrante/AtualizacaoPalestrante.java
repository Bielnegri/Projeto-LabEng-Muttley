package com.fatec.muttley.palestrante;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoPalestrante(
        Long id,

        @NotBlank(message = "Resumo profissional é obrigatório")
        String resumoProfissional,

        @NotBlank(message = "Empresa atual é obrigatória")
        String empresaAtual,

        @NotBlank(message = "Cargo é obrigatório")
        String cargo
) {

}
