package com.fatec.muttley.participante;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    @Mapping(target = "alunoId", source = "aluno.id")
    @Mapping(target = "eventoId", source = "evento.id")
    AtualizacaoParticipante toAtualizacaoDto(Participante participante);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aluno", ignore = true)
    @Mapping(target = "evento", ignore = true)
    Participante toEntityFromAtualizacao(AtualizacaoParticipante dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aluno", ignore = true)
    @Mapping(target = "evento", ignore = true)
    void updateEntityFromDto(AtualizacaoParticipante dto, @MappingTarget Participante participante);
}
