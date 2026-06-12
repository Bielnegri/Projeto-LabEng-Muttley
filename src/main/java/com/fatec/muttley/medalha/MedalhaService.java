package com.fatec.muttley.medalha;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MedalhaService {
    @Autowired
    private MedalhaRepository medalhaRepository;

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private MedalhaMapper medalhaMapper;

    public Medalha salvarOuAtualizar(AtualizacaoMedalha dto) {
        Participacao participacao = participacaoService.procurarPorId(dto.participacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada com ID: " + dto.participacaoId()));
        if (dto.id() != null) {
            Medalha existente = medalhaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada com ID: " + dto.id()));
            medalhaMapper.updateEntityFromDto(dto, existente);
            existente.setParticipacao(participacao);
            return medalhaRepository.save(existente);
        } else {
            Medalha novoMedalha = medalhaMapper.toEntityFromAtualizacao(dto);
            novoMedalha.setParticipacao(participacao);

            return medalhaRepository.save(novoMedalha);
        }
    }

    public List<Medalha> procurarTodos(){
        return medalhaRepository.findAll(Sort.by("nome").ascending());
    }

    public List<Medalha> procurarPorPessoa(Long pessoaId) {
        return medalhaRepository.findByPessoaIdComDados(pessoaId);
    }

    public Medalha gerarMedalhaBronzePorPresenca(Participacao participacao) {
        if (participacao == null || participacao.getId() == null || !participacao.isPresente()) {
            throw new IllegalArgumentException("A participacao precisa estar confirmada para receber a medalha.");
        }

        if (medalhaRepository.existsByParticipacaoIdAndTipo(participacao.getId(), TipoMedalha.BRONZE)) {
            return null;
        }

        Medalha medalha = new Medalha();
        medalha.setNome("Participacao confirmada");
        medalha.setDescricao("Medalha concedida pela presenca confirmada no evento.");
        medalha.setTipo(TipoMedalha.BRONZE);
        medalha.setParticipacao(participacao);
        return medalhaRepository.save(medalha);
    }

    public void gerarMedalhasBronzePorPresenca(List<Participacao> participacoes) {
        participacoes.stream()
                .filter(Participacao::isPresente)
                .forEach(this::gerarMedalhaBronzePorPresenca);
    }

    public List<MedalhaRepository.MedalhasPorParticipante> procurarTotaisPorParticipante(int limite) {
        return medalhaRepository.findTotaisPorParticipante(PageRequest.of(0, limite));
    }

    public void apagarPorId (Long id) {
        medalhaRepository.deleteById(id);
    }

    public Optional<Medalha> procurarPorId(Long id) {
        return medalhaRepository.findById(id);
    }
}
