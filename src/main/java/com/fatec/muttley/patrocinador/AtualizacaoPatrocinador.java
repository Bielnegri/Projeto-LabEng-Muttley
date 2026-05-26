package com.fatec.muttley.patrocinador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoPatrocinador(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotNull(message = "Valor do patrocínio é obrigatório")
        Double valorPatrocinio,

        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotBlank(message = "Site é obrigatório")
        String site
) {
        public AtualizacaoPatrocinador withId(Long id) {
                return new AtualizacaoPatrocinador(id, this.nome(), this.cnpj(), this.valorPatrocinio(), this.email(), this.telefone(), this.site());
        }
}
