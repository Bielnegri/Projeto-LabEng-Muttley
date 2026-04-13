package com.fatec.muttley.medalha;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.fatec.muttley.aluno.Aluno;

@Mapper(componentModel = "spring")
public interface MedalhaMapper {

    // Converte Entity para DTO (para preencher formulário de edição)
    @Mapping(target = "alunoId", source = "aluno.id")
    AtualizacaoMedalha toAtualizacaoDto(Medalha medalha);

    // Converte DTO para Entity (para criação NOVA - ignora ID)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aluno", source = "alunoId", qualifiedByName = "idToAluno")
    Medalha toEntityFromAtualizacao(AtualizacaoMedalha dto);

    // Atualiza Entity existente com dados do DTO
    @Mapping(target = "id", ignore = true) // Não atualiza ID
    @Mapping(target = "aluno", source = "alunoId", qualifiedByName = "idToAluno")
    void updateEntityFromDto(AtualizacaoMedalha dto, @MappingTarget Medalha medalha);

    // Métod0 para converter alunoId em objeto Aluno
    @Named("idToAluno")
    default Aluno idToAluno(Long alunoId) {
        if (alunoId == null) return null;
        Aluno aluno = new Aluno();
        aluno.setId(alunoId);
        return aluno;
    }
}
