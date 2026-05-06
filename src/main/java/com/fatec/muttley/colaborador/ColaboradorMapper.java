package com.fatec.muttley.colaborador;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {
    AtualizacaoColaborador toAtualizacaoDto(Colaborador colaborador);

    @Mapping(target = "id", ignore = true)
    Colaborador toEntityFromAtualizacao(AtualizacaoColaborador dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoColaborador dto, @MappingTarget Colaborador colaborador);
}
