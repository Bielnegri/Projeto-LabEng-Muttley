package com.fatec.muttley.professor;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoProfessor(
        Long id,

        @NotBlank(message = "Área de formação é obrigatória")
        String areaFormacao,

        @NotBlank(message = "Titulação é obrigatória")
        String titulacao
) {

}
