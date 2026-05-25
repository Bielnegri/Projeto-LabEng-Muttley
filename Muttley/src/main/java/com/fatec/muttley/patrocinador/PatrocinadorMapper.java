package com.fatec.muttley.patrocinador;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatrocinadorMapper {
    AtualizacaoPatrocinador toAtualizacaoDto(Patrocinador patrocinador);

    @Mapping(target = "id", ignore = true)
    Patrocinador toEntityFromAtualizacao(AtualizacaoPatrocinador dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoPatrocinador dto, @MappingTarget Patrocinador patrocinador);
}
