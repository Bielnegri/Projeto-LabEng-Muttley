package com.fatec.muttley.disciplina;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DisciplinaMapper {
    AtualizacaoDisciplina toAtualizacaoDto(Disciplina disciplina);

    @Mapping(target = "id", ignore = true)
    Disciplina toEntityFromAtualizacao(AtualizacaoDisciplina dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoDisciplina dto, @MappingTarget Disciplina disciplina);
}
