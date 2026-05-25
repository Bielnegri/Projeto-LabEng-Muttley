package com.fatec.muttley.local;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.fatec.muttley.endereco.Endereco;

@Mapper(componentModel = "spring")
public interface LocalMapper {

    @Mapping(target = "enderecoId", source = "endereco.id")
    AtualizacaoLocal toAtualizacaoDto(Local local);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", source = "enderecoId", qualifiedByName = "idToEndereco")
    Local toEntityFromAtualizacao(AtualizacaoLocal dto);

    @Mapping(target = "id", ignore = true) // Não atualiza ID
    @Mapping(target = "endereco", source = "enderecoId", qualifiedByName = "idToEndereco")
    void updateEntityFromDto(AtualizacaoLocal dto, @MappingTarget Local local);

    @Named("idToEndereco")
    default Endereco idToEndereco(Long enderecoId) {
        if (enderecoId == null) return null;
        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        return endereco;
    }
}
