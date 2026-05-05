package com.fatec.muttley.evento;

import java.util.List;
import java.util.Optional;

import com.fatec.muttley.local.Local;
import com.fatec.muttley.local.LocalService;
import com.fatec.muttley.patrocinador.Patrocinador;
import com.fatec.muttley.patrocinador.PatrocinadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.disciplina.DisciplinaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private PatrocinadorService patrocinadorService;

    @Autowired
    private LocalService localService;

    @Autowired
    private EventoMapper eventoMapper;

    public Evento salvarOuAtualizar(AtualizacaoEvento dto) {
        Disciplina disciplina = disciplinaService.procurarPorId(dto.disciplinaId())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada com ID: " + dto.disciplinaId()));
        Patrocinador patrocinador = patrocinadorService.procurarPorId(dto.patrocinadorId())
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrada com ID: " + dto.patrocinadorId()));
        Local local = localService.procurarPorId(dto.localId())
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrada com ID: " + dto.localId()));
        if (dto.id() != null) {
            Evento existente = eventoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Evento não encontrada com ID: " + dto.id()));
            eventoMapper.updateEntityFromDto(dto, existente);
            existente.setDisciplina(disciplina);
            existente.setPatrocinador(patrocinador);
            existente.setLocal(local);
            return eventoRepository.save(existente);
        } else {
            Evento novoEvento = eventoMapper.toEntityFromAtualizacao(dto);
            novoEvento.setDisciplina(disciplina);
            novoEvento.setPatrocinador(patrocinador);
            novoEvento.setLocal(local);
            return eventoRepository.save(novoEvento);
        }
    }

    public List<Evento> procurarTodos(){
        return eventoRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId (Long id) {
        eventoRepository.deleteById(id);
    }

    public Optional<Evento> procurarPorId(Long id) {
        return eventoRepository.findById(id);
    }
}