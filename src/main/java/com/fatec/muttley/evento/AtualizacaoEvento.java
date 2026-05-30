package com.fatec.muttley.evento;

import java.time.format.DateTimeParseException;
import com.fatec.muttley.evento.enums.ModalidadeEventoEnum;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AtualizacaoEvento(
        Long id,

        @NotBlank(message = "Tema e obrigatorio")
        String tema,

        @NotBlank(message = "Descricao e obrigatoria")
        String descricao,

        @NotNull(message = "Data e obrigatoria")
        @FutureOrPresent(message = "A data do evento não pode estar no passado")
        LocalDate data,

        @NotBlank(message = "Horario de inicio e obrigatorio")
        String horarioInicio,

        @NotBlank(message = "Horario de fim e obrigatorio")
        String horarioFim,

        @NotNull(message = "Modalidade e obrigatoria")
        ModalidadeEventoEnum modalidade,

        StatusEventoEnum status,

        @NotNull(message = "Disciplina e obrigatoria")
        Long disciplinaId,

        @NotNull(message = "Patrocinador e obrigatorio")
        Long patrocinadorId,

        @NotNull(message = "Local e obrigatorio")
        Long localId
) {
        public AtualizacaoEvento withId(Long id) {
                return new AtualizacaoEvento(id, this.tema(), this.descricao(), this.data(), this.horarioInicio(), this.horarioFim(), this.modalidade(), this.status(), this.disciplinaId(), this.patrocinadorId(), this.localId());
        }
}
