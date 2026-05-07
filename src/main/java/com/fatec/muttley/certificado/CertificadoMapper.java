package com.fatec.muttley.certificado;

import com.fatec.muttley.participacao.Participacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {

    @Mapping(target = "participacaoId", source = "participacao.id")
    AtualizacaoCertificado toAtualizacaoDto(Certificado certificado);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoValidacao", ignore = true)
    @Mapping(target = "urlPublica", ignore = true)
    @Mapping(target = "caminhoPdf", ignore = true)
    @Mapping(target = "participacao", source = "participacaoId", qualifiedByName = "idToParticipacao")
    Certificado toEntityFromAtualizacao(AtualizacaoCertificado dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoValidacao", ignore = true)
    @Mapping(target = "urlPublica", ignore = true)
    @Mapping(target = "caminhoPdf", ignore = true)
    @Mapping(target = "participacao", source = "participacaoId", qualifiedByName = "idToParticipacao")
    void updateEntityFromDto(AtualizacaoCertificado dto, @MappingTarget Certificado certificado);

    @Named("idToParticipacao")
    default Participacao idToParticipacao(Long participacaoId) {
        if (participacaoId == null) {
            return null;
        }
        Participacao participacao = new Participacao();
        participacao.setId(participacaoId);
        return participacao;
    }
}
