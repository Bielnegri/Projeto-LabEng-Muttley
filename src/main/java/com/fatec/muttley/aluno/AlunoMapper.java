package com.fatec.muttley.aluno;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlunoMapper {
    AtualizacaoAluno toAtualizacaoDto(Aluno aluno);

    @Mapping(target = "id", ignore = true)
    Aluno toEntityFromAtualizacao(AtualizacaoAluno dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoAluno dto, @MappingTarget Aluno aluno);
}
