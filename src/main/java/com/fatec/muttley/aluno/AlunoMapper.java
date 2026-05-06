package com.fatec.muttley.aluno;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlunoMapper {
    // Converte Entity para DTO (para preencher formulário de edição)
    AtualizacaoAluno toAtualizacaoDto(Aluno aluno);

    // Converte DTO para Entity (para criação NOVA - ignora ID)
    @Mapping(target = "id", ignore = true)
    Aluno toEntityFromAtualizacao(AtualizacaoAluno dto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true) // Não atualiza ID
    void updateEntityFromDto(AtualizacaoAluno dto, @MappingTarget Aluno aluno);
}
