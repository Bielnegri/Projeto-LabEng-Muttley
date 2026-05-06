package com.fatec.muttley.aluno;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoAluno(
     Long id,

     @NotBlank(message = "Instituição é obrigatória")
     String instituicao,

     @NotBlank(message = "Matrícula é obrigatória")
     String matricula
) {

}
