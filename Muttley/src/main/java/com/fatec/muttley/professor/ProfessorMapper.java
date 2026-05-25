package com.fatec.muttley.professor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    AtualizacaoProfessor toAtualizacaoDto(Professor professor);

    @Mapping(target = "id", ignore = true)
    Professor toEntityFromAtualizacao(AtualizacaoProfessor dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoProfessor dto, @MappingTarget Professor professor);
}
