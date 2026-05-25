package com.fatec.muttley.participacao;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParticipacaoMapper {

    @Mapping(target = "pessoaId", source = "pessoa.id")
    @Mapping(target = "eventoId", source = "evento.id")
    AtualizacaoParticipacao toAtualizacaoDto(Participacao participacao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pessoa", ignore = true)
    @Mapping(target = "evento", ignore = true)
    Participacao toEntityFromAtualizacao(AtualizacaoParticipacao dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pessoa", ignore = true)
    @Mapping(target = "evento", ignore = true)
    void updateEntityFromDto(AtualizacaoParticipacao dto, @MappingTarget Participacao participacao);
}
