package com.fatec.muttley.certificado;

import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.sql.Date;
import java.time.LocalDate;

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
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada com ID: " + dto.participacaoId()));
        if (dto.id() != null) {
            Certificado existente = certificadoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado com ID: " + dto.id()));
            certificadoMapper.updateEntityFromDto(dto, existente);
            existente.setParticipacao(participacao);
            preencherDadosPublicos(existente);
            return certificadoRepository.save(existente);
        }

        Certificado novoCertificado = certificadoMapper.toEntityFromAtualizacao(dto);
        novoCertificado.setParticipacao(participacao);
        preencherDadosPublicos(novoCertificado);
        return certificadoRepository.save(novoCertificado);
    }

    @Transactional
    public List<Certificado> procurarTodos() {
        List<Certificado> certificados = certificadoRepository.findAll(Sort.by("assinatura").ascending());
        garantirDadosPublicos(certificados);
        return certificados;
    }

    @Transactional
    public List<Certificado> procurarUltimosEmitidos() {
        List<Certificado> certificados = certificadoRepository.findUltimosEmitidos(PageRequest.of(0, 8));
        garantirDadosPublicos(certificados);
        return certificados;
    }

    public List<CertificadoRepository.CertificadosPorEvento> procurarTotaisPorEvento(int limite) {
        return certificadoRepository.findTotaisPorEvento(PageRequest.of(0, limite));
    }

    public long contarEmitidosDesde(LocalDate dataInicio) {
        return certificadoRepository.countByDataEmissaoGreaterThanEqual(Date.valueOf(dataInicio));
    }

    public long contarEmitidosEntre(LocalDate dataInicio, LocalDate dataFimExclusiva) {
        return certificadoRepository.countByDataEmissaoGreaterThanEqualAndDataEmissaoLessThan(
                Date.valueOf(dataInicio),
                Date.valueOf(dataFimExclusiva)
        );
    }

    public void apagarPorId(Long id) {
        certificadoRepository.deleteById(id);
    }

    public Optional<Certificado> procurarPorId(Long id) {
        return certificadoRepository.findById(id);
    }

    public Optional<Certificado> procurarPorCodigoValidacao(String codigoValidacao) {
        return certificadoRepository.findByCodigoValidacaoComDados(codigoValidacao);
    }

    @Transactional
    public List<Certificado> gerarCertificadosParaParticipacoes(List<Long> participacaoIds) {
        List<Certificado> certificadosGerados = new java.util.ArrayList<>();
        for (Long participacaoId : participacaoIds) {
            if (participacaoId == null || certificadoRepository.existsByParticipacaoId(participacaoId)) {
                continue;
            }

            Participacao participacao = participacaoService.procurarPorId(participacaoId)
                    .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada com ID: " + participacaoId));

            Certificado certificado = new Certificado();
            certificado.setDataEmissao(LocalDate.now());
            certificado.setAssinatura("Coordenação FATEC");
            certificado.setParticipacao(participacao);
            preencherDadosPublicos(certificado);
            certificadosGerados.add(certificadoRepository.save(certificado));
        }
        return certificadosGerados;
    }

    private void preencherDadosPublicos(Certificado certificado) {
        if (certificado.getCodigoValidacao() == null || certificado.getCodigoValidacao().isBlank()) {
            certificado.setCodigoValidacao(gerarCodigoValidacaoUnico());
        }
        if (certificado.getUrlPublica() == null || certificado.getUrlPublica().isBlank()) {
            certificado.setUrlPublica("/certificados/" + certificado.getCodigoValidacao());
        }
        if (certificado.getCaminhoPdf() == null || certificado.getCaminhoPdf().isBlank()) {
            certificado.setCaminhoPdf("/certificados/" + certificado.getCodigoValidacao() + ".pdf");
        }
    }

    private String gerarCodigoValidacaoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString();
        } while (certificadoRepository.existsByCodigoValidacao(codigo));
        return codigo;
    }

    private void garantirDadosPublicos(List<Certificado> certificados) {
        for (Certificado certificado : certificados) {
            boolean estavaIncompleto = certificado.getCodigoValidacao() == null
                    || certificado.getCodigoValidacao().isBlank()
                    || certificado.getUrlPublica() == null
                    || certificado.getUrlPublica().isBlank()
                    || certificado.getCaminhoPdf() == null
                    || certificado.getCaminhoPdf().isBlank();
            if (estavaIncompleto) {
                preencherDadosPublicos(certificado);
                certificadoRepository.save(certificado);
            }
        }
    }
}
