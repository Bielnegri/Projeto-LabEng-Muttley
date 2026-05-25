package com.fatec.muttley.endereco;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {
    AtualizacaoEndereco toAtualizacaoDto(Endereco endereco);

    @Mapping(target = "id", ignore = true)
    Endereco toEntityFromAtualizacao(AtualizacaoEndereco dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoEndereco dto, @MappingTarget Endereco endereco);
}
