package com.fatec.muttley.evento;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.fatec.muttley.local.Local;
import com.fatec.muttley.local.LocalService;
import com.fatec.muttley.patrocinador.Patrocinador;
import com.fatec.muttley.patrocinador.PatrocinadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (dto.disciplinaId() == null)
            throw new EntityNotFoundException("Disciplina é obrigatória.");
        if (dto.patrocinadorId() == null)
            throw new EntityNotFoundException("Patrocinador é obrigatório.");
        if (dto.localId() == null)
            throw new EntityNotFoundException("Local é obrigatório.");

        Disciplina disciplina = disciplinaService.procurarPorId(dto.disciplinaId())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada com ID: " + dto.disciplinaId()));
        Patrocinador patrocinador = patrocinadorService.procurarPorId(dto.patrocinadorId())
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado com ID: " + dto.patrocinadorId()));
        Local local = localService.procurarPorId(dto.localId())
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado com ID: " + dto.localId()));
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
        return eventoRepository.findAll(Sort.by("tema").ascending());
    }

    public void apagarPorId (Long id) {
        eventoRepository.deleteById(id);
    }

    public Optional<Evento> procurarPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public List<Evento> procurarTodosOrdenarPorInicio() {
        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        return eventoRepository.findProximosEventos(Date.valueOf(LocalDate.now()), horaAtual, PageRequest.of(0, 8));
    }

    public List<Evento> procurarEventosEncerradosAguardandoCertificado() {
        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        return eventoRepository.findEventosEncerrados(Date.valueOf(LocalDate.now()), horaAtual, PageRequest.of(0, 10));
    }

    public Page<Evento> procurarProximosFiltrados(String busca, Pageable pageable) {
        String horaAtual = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String termoBusca = (busca == null) ? "" : busca;
        return eventoRepository.findProximosEventosFiltrados(
                Date.valueOf(LocalDate.now()), horaAtual, termoBusca, pageable);
    }
}
