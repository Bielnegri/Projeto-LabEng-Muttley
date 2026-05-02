package com.fatec.muttley.aluno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoAluno(
     Long id,

     @NotNull(message = "Instituição é obrigatória")
     String instituicao,

     @NotBlank(message = "Matrícula é obrigatória")
     String matricula
) {

}
