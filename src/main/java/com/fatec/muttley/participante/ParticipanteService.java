package com.fatec.muttley.participante;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.aluno.AlunoService;
import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.EventoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private ParticipanteMapper participanteMapper;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private EventoService eventoService;

    public Participante salvarOuAtualizar(AtualizacaoParticipante dto) {
        Aluno aluno = alunoService.procurarPorId(dto.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno nao encontrado com o id: " + dto.alunoId()));
        Evento evento = eventoService.procurarPorId(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento nao encontrado com o id: " + dto.eventoId()));

        if (dto.id() != null) {
            Participante existente = participanteRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Participante nao encontrado com o id: " + dto.id()));
            participanteMapper.updateEntityFromDto(dto, existente);
            existente.setAluno(aluno);
            existente.setEvento(evento);
            return participanteRepository.save(existente);
        } else {
            Participante novoParticipante = participanteMapper.toEntityFromAtualizacao(dto);
            novoParticipante.setAluno(aluno);
            novoParticipante.setEvento(evento);
            return participanteRepository.save(novoParticipante);
        }
    }

    public List<Participante> procurarTodos() {
        return participanteRepository.findAll(Sort.by("inscricao").ascending());
    }

    public void apagarPorId(Long id) {
        participanteRepository.deleteById(id);
    }

    public Optional<Participante> procurarPorId(Long id) {
        return participanteRepository.findById(id);
    }
}
