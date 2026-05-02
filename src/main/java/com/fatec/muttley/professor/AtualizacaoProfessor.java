package com.fatec.muttley.professor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoProfessor(
        Long id,

        @NotNull(message = "Área de formação é obrigatória")
        String areaFormacao,

        @NotBlank(message = "Titulação é obrigatória")
        String titulacao
) {

}
