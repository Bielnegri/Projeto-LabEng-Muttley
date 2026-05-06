package com.fatec.muttley.certificado;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CertificadoService {
    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private CertificadoMapper certificadoMapper;

    public Certificado salvarOuAtualizar(AtualizacaoCertificado dto) {
        Participacao participacao = participacaoService.procurarPorId(dto.participacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Participacao não encontrada com ID: " + dto.participacaoId()));
        if (dto.id() != null) {
            Certificado existente = certificadoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado com ID: " + dto.id()));
            certificadoMapper.updateEntityFromDto(dto, existente);
            existente.setParticipacao(participacao);
            return certificadoRepository.save(existente);
        } else {
            Certificado novoCertificado = certificadoMapper.toEntityFromAtualizacao(dto);
            novoCertificado.setParticipacao(participacao);

            return certificadoRepository.save(novoCertificado);
        }
    }

    public List<Certificado> procurarTodos(){
        return certificadoRepository.findAll(Sort.by("assinatura").ascending());
    }

    public void apagarPorId (Long id) {
        certificadoRepository.deleteById(id);
    }

    public Optional<Certificado> procurarPorId(Long id) {
        return certificadoRepository.findById(id);
    }
}