package com.fatec.muttley.participante;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    AtualizacaoParticipante toAtualizacaoDto(Participante participante);

    @Mapping(target = "id", ignore = true)
    Participante toEntityFromAtualizacao(AtualizacaoParticipante dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoParticipante dto, @MappingTarget Participante participante);
}