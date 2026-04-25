package com.fatec.muttley.pessoa;

import io.github.andrelamego.brValidator.annotation.ValidCpf;
import jakarta.validation.constraints.NotBlank;

public record AtualizacaoPessoa(
        Long id,
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "CPF é obrigatório")
        @ValidCpf
        String cpf
) {}