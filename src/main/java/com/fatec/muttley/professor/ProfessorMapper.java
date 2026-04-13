package com.fatec.muttley.professor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    // Converte Entity para DTO (para preencher formulário de edição)
    AtualizacaoProfessor toAtualizacaoDto(Professor professor);

    // Converte DTO para Entity (para criação NOVA - ignora ID)
    @Mapping(target = "id", ignore = true)
    Professor toEntityFromAtualizacao(AtualizacaoProfessor dto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true) // Não atualiza ID
    void updateEntityFromDto(AtualizacaoProfessor dto, @MappingTarget Professor professor);
}
