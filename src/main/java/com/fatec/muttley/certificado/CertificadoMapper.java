package com.fatec.muttley.certificado;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {
    AtualizacaoCertificado toAtualizacaoDto(Certificado certificado);

    @Mapping(target = "id", ignore = true)
    Certificado toEntityFromAtualizacao(AtualizacaoCertificado dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AtualizacaoCertificado dto, @MappingTarget Certificado certificado);
}
