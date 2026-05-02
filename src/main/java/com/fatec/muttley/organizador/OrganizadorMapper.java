package com.fatec.muttley.organizador;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizadorMapper {
    AtualizacaoOrganizador toAtualizacaoDto(Organizador organizador);

    @Mapping(target = "id", ignore = true)
    Organizador toEntityFromAtualizacao(AtualizacaoOrganizador dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoOrganizador dto, @MappingTarget Organizador organizador);
}
