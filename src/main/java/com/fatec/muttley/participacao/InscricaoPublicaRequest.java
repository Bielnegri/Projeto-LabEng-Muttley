package com.fatec.muttley.participacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InscricaoPublicaRequest(
        @NotBlank(message = "Nome completo e obrigatorio")
        String nomeCompleto,

        @NotBlank(message = "CPF e obrigatorio")
        String cpf,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email
) {
}
