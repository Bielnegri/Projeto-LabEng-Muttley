package com.fatec.muttley.participacao;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipacaoService {

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    @Autowired
    private ParticipacaoMapper participacaoMapper;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EventoService eventoService;

    public Participacao salvarOuAtualizar(AtualizacaoParticipacao dto) {
        Pessoa pessoa = pessoaService.procurarPorId(dto.pessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa nao encontrada com o id: " + dto.pessoaId()));
        Evento evento = eventoService.procurarPorId(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento nao encontrado com o id: " + dto.eventoId()));

        if (dto.id() != null) {
            Participacao existente = participacaoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Participacao nao encontrada com o id: " + dto.id()));
            participacaoMapper.updateEntityFromDto(dto, existente);
            existente.setPessoa(pessoa);
            existente.setEvento(evento);
            return participacaoRepository.save(existente);
        } else {
            Participacao novoParticipacao = participacaoMapper.toEntityFromAtualizacao(dto);
            novoParticipacao.setPessoa(pessoa);
            novoParticipacao.setEvento(evento);
            return participacaoRepository.save(novoParticipacao);
        }
    }

    public List<Participacao> procurarTodos() {
        return participacaoRepository.findAll(Sort.by("inscricao").ascending());
    }

    public void apagarPorId(Long id) {
        participacaoRepository.deleteById(id);
    }

    public Optional<Participacao> procurarPorId(Long id) {
        return participacaoRepository.findById(id);
    }
}
