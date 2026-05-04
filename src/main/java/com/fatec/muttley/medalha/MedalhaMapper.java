package com.fatec.muttley.medalha;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.fatec.muttley.aluno.Aluno;

@Mapper(componentModel = "spring")
public interface MedalhaMapper {

    @Mapping(target = "alunoId", source = "aluno.id")
    AtualizacaoMedalha toAtualizacaoDto(Medalha medalha);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aluno", source = "alunoId", qualifiedByName = "idToAluno")
    Medalha toEntityFromAtualizacao(AtualizacaoMedalha dto);

    @Mapping(target = "id", ignore = true) // Não atualiza ID
    @Mapping(target = "aluno", source = "alunoId", qualifiedByName = "idToAluno")
    void updateEntityFromDto(AtualizacaoMedalha dto, @MappingTarget Medalha medalha);

    @Named("idToAluno")
    default Aluno idToAluno(Long alunoId) {
        if (alunoId == null) return null;
        Aluno aluno = new Aluno();
        aluno.setId(alunoId);
        return aluno;
    }
}
