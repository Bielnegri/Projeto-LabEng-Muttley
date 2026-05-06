package com.fatec.muttley.pessoa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PessoaMapper {
    // Converte Entity para DTO (para preencher formulário de edição)
    AtualizacaoPessoa toAtualizacaoDto(Pessoa pessoa);

    // Converte DTO para Entity (para criação NOVA - ignora ID)
    @Mapping(target = "id", ignore = true)
    Pessoa toEntityFromAtualizacao(AtualizacaoPessoa dto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true) // Não atualiza ID
    void updateEntityFromDto(AtualizacaoPessoa dto, @MappingTarget Pessoa pessoa);
}
