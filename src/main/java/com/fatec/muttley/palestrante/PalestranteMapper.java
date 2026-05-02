package com.fatec.muttley.palestrante;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PalestranteMapper {
    AtualizacaoPalestrante toAtualizacaoDto(Palestrante palestrante);

    @Mapping(target = "id", ignore = true)
    Palestrante toEntityFromAtualizacao(AtualizacaoPalestrante dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoPalestrante dto, @MappingTarget Palestrante palestrante);
}
