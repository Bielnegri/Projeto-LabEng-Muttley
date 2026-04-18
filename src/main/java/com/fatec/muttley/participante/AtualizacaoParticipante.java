package com.fatec.muttley.participante;

import jakarta.validation.constraints.NotNull;

public record AtualizacaoParticipante(
        Long id,
        int inscricao,
        @NotNull(message = "Aluno é obrigatório")
        Long alunoId,
        @NotNull(message = "Evento é obrigatório")
        Long eventoId
) {}
