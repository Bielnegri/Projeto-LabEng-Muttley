package com.fatec.muttley.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoEndereco(
        Long id,

        @NotBlank(message = "Estado é obrigatório")
        String estado,

        @NotBlank(message = "Cidade é obrigatório")
        String cidade,

        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        @NotNull(message = "Número é obrigatório")
        int numero,

        @NotBlank(message = "Complemento é obrigatório")
        String complemento
) {
        public AtualizacaoEndereco withId(Long id) {
                return new AtualizacaoEndereco(id, this.estado(), this.cidade(), this.bairro(), this.logradouro(), this.numero(), this.complemento());
        }
}
