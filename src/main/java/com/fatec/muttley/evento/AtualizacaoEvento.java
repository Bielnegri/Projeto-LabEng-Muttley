package com.fatec.muttley.evento;

import java.sql.Date;

public record AtualizacaoEvento(
        Long id,
        String tema,
        String local,
        Date data,
        String horario,
        Long disciplinaId
) {}
