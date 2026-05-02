package com.fatec.muttley.palestrante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoPalestrante(
        Long id,

        @NotNull(message = "Resumo profissional é obrigatório")
        String resumoProfissional,

        @NotBlank(message = "Empresa atual é obrigatória")
        String empresaAtual,

        @NotBlank(message = "Cargo é obrigatório")
        String cargo
) {

}
