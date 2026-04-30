package com.fatec.muttley.participacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoParticipacao(
        Long id,
        @NotBlank(message = "Inscrição é obrigatório")
        int inscricao,

        @NotBlank(message = "Tipo é obrigatório")
        String tipo,

        @NotNull(message = "Pessoa é obrigatório")
        Long pessoaId,

        @NotNull(message = "Evento é obrigatório")
        Long eventoId
) {}
