package com.fatec.muttley.evento;

import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.disciplina.DisciplinaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoMapper eventoMapper;

    @Autowired
    private DisciplinaService disciplinaService;

    public Evento salvarOuAtualizar(AtualizacaoEvento dto) {
        Disciplina disciplina = null;
        if (dto.disciplinaId() != null) {
            disciplina = disciplinaService.procurarPorId(dto.disciplinaId())
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina nao encontrada com id: " + dto.disciplinaId()));
        }

        if (dto.id() != null) {
            Evento existente = eventoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Evento nao encontrado com id: " + dto.id()));
            eventoMapper.updateEntityFromDto(dto, existente);
            existente.setDisciplina(disciplina);
            return eventoRepository.save(existente);
        } else {
            Evento novoEvento = eventoMapper.toEntityFromAtualizacao(dto);
            novoEvento.setDisciplina(disciplina);
            return eventoRepository.save(novoEvento);
        }
    }

    public List<Evento> procurarTodos() {
        return eventoRepository.findAll(Sort.by("tema").ascending());
    }

    public void apagarPorId(Long id) {
        eventoRepository.deleteById(id);
    }

    public Optional<Evento> procurarPorId(Long id) {
        return eventoRepository.findById(id);
    }
}
