package com.fatec.muttley.certificado;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CertificadoService {
    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private CertificadoMapper certificadoMapper;

    public Certificado salvarOuAtualizar(AtualizacaoCertificado dto){
        if (dto.id() != null){
            Certificado existente = certificadoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado com id: ." + dto.id()));
            certificadoMapper.updateEntityFromDto(dto, existente);
            return  certificadoRepository.save(existente);
        } else {
            Certificado novoCertificado = certificadoMapper.toEntityFromAtualizacao(dto);
            return certificadoRepository.save(novoCertificado);
        }
    }

    public List<Certificado> procurarTodos(){
        return certificadoRepository.findAll(Sort.by("id").ascending());
    }

    public void apagarPorId(Long id){
        certificadoRepository.deleteById(id);
    }

    public Optional<Certificado> procurarPorId(Long id){
        return certificadoRepository.findById(id);
    }
}
