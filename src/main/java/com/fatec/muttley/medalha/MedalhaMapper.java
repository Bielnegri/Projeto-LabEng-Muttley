package com.fatec.muttley.medalha;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.fatec.muttley.participacao.Participacao;

@Mapper(componentModel = "spring")
public interface MedalhaMapper {

    @Mapping(target = "participacaoId", source = "participacao.id")
    AtualizacaoMedalha toAtualizacaoDto(Medalha medalha);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "participacao", source = "participacaoId", qualifiedByName = "idToParticipacao")
    Medalha toEntityFromAtualizacao(AtualizacaoMedalha dto);

    @Mapping(target = "id", ignore = true) // Não atualiza ID
    @Mapping(target = "participacao", source = "participacaoId", qualifiedByName = "idToParticipacao")
    void updateEntityFromDto(AtualizacaoMedalha dto, @MappingTarget Medalha medalha);

    @Named("idToParticipacao")
    default Participacao idToParticipacao(Long participacaoId) {
        if (participacaoId == null) return null;
        Participacao participacao = new Participacao();
        participacao.setId(participacaoId);
        return participacao;
    }
}
