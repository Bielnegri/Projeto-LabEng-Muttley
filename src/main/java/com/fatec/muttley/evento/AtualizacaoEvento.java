package com.fatec.muttley.evento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public record AtualizacaoEvento (
        Long id,

        @NotBlank(message = "Tema é obrigatório")
        String tema,

        @NotNull(message = "Data é obrigatória")
        Date data,

        @NotBlank(message = "Horário de início é obrigatório")
        String horarioInicio,

        @NotBlank(message = "Horário de fim é obrigatório")
        String horarioFim,

        @NotBlank(message = "Modalidade é obrigatória")
        String modalidade,

        @NotNull(message = "Disciplina é obrigatória")
        Long disciplinaId,

        @NotNull(message = "Patrocinador é obrigatório")
        Long patrocinadorId,

        @NotNull(message = "Local é obrigatório")
        Long localId
) {}

