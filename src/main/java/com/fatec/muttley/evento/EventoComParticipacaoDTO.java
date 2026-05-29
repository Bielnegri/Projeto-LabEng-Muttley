package com.fatec.muttley.evento;

import com.fatec.muttley.participacao.AtualizacaoParticipacaoNovoEvento;
import jakarta.validation.Valid;

import java.util.List;

public record EventoComParticipacaoDTO(
        @Valid AtualizacaoEvento evento,
        @Valid List<AtualizacaoParticipacaoNovoEvento> participacoes
) {}
