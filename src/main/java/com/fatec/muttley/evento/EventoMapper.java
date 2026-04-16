package com.fatec.muttley.evento;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    AtualizacaoEvento toAtualizacaoDto(Evento evento);

    @Mapping(target = "id", ignore = true)
    Evento toEntityFromAtualizacao(AtualizacaoEvento dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoEvento dto, @MappingTarget Evento evento);
}