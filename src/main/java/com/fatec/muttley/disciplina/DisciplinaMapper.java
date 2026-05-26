package com.fatec.muttley.disciplina;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DisciplinaMapper {
    @Mapping(target = "professor", ignore = true)
    Disciplina toEntity(AtualizacaoDisciplina dto);

    @Mapping(target = "id_professor", source = "professor.id")
    AtualizacaoDisciplina toAtualizacaoDto(Disciplina disciplina);

    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "id", ignore = true)
    Disciplina toEntityFromAtualizacao(AtualizacaoDisciplina dto);

    @Mapping(target = "professor", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoDisciplina dto, @MappingTarget Disciplina disciplina);
}
