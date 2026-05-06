package com.fatec.muttley.evento;

import com.fatec.muttley.local.Local;
import com.fatec.muttley.patrocinador.Patrocinador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.fatec.muttley.disciplina.Disciplina;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "disciplinaId", source = "disciplina.id")
    @Mapping(target = "patrocinadorId", source = "patrocinador.id")
    @Mapping(target = "localId", source = "local.id")
    AtualizacaoEvento toAtualizacaoDto(Evento evento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplina", source = "disciplinaId", qualifiedByName = "idToDisciplina")
    @Mapping(target = "patrocinador", source = "patrocinadorId", qualifiedByName = "idToPatrocinador")
    @Mapping(target = "local", source = "localId", qualifiedByName = "idToLocal")
    Evento toEntityFromAtualizacao(AtualizacaoEvento dto);

    @Mapping(target = "id", ignore = true) // Não atualiza ID
    @Mapping(target = "disciplina", source = "disciplinaId", qualifiedByName = "idToDisciplina")
    @Mapping(target = "patrocinador", source = "patrocinadorId", qualifiedByName = "idToPatrocinador")
    @Mapping(target = "local", source = "localId", qualifiedByName = "idToLocal")
    void updateEntityFromDto(AtualizacaoEvento dto, @MappingTarget Evento evento);

    @Named("idToDisciplina")
    default Disciplina idToDisciplina(Long disciplinaId) {
        if (disciplinaId == null) return null;
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);
        return disciplina;
    }

    @Named("idToPatrocinador")
    default Patrocinador idToPatrocinador(Long patrocinadorId) {
        if (patrocinadorId == null) return null;
        Patrocinador patrocinador = new Patrocinador();
        patrocinador.setId(patrocinadorId);
        return patrocinador;
    }

    @Named("idToLocal")
    default Local idToLocal(Long localId) {
        if (localId == null) return null;
        Local local = new Local();
        local.setId(localId);
        return local;
    }
}
