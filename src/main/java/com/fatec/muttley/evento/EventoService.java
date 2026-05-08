package com.fatec.muttley.evento;

import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.disciplina.DisciplinaService;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.local.Local;
import com.fatec.muttley.local.LocalService;
import com.fatec.muttley.patrocinador.Patrocinador;
import com.fatec.muttley.patrocinador.PatrocinadorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
            throw new EntityNotFoundException("Patrocinador é obrigatória.");
        if (dto.localId() == null)
            throw new EntityNotFoundException("Local é obrigatória.");

        Disciplina disciplina = disciplinaService.procurarPorId(dto.disciplinaId())
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada com ID: " + dto.disciplinaId()));
        Patrocinador patrocinador = patrocinadorService.procurarPorId(dto.patrocinadorId())
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado com ID: " + dto.patrocinadorId()));
        Local local = localService.procurarPorId(dto.localId())
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado com ID: " + dto.localId()));
        if (dto.id() != null) {
            Evento existente = eventoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com ID: " + dto.id()));
            eventoMapper.updateEntityFromDto(dto, existente);
            existente.setDisciplina(disciplina);
            existente.setPatrocinador(patrocinador);
            existente.setLocal(local);
            return eventoRepository.save(existente);
        }

        Evento novoEvento = eventoMapper.toEntityFromAtualizacao(dto);
        novoEvento.setStatus(StatusEventoEnum.CRIADO);
        novoEvento.setDisciplina(disciplina);
        novoEvento.setPatrocinador(patrocinador);
        novoEvento.setLocal(local);
        return eventoRepository.save(novoEvento);
    }

    public List<Evento> procurarTodos() {
        return eventoRepository.findAll(Sort.by("tema").ascending()).stream()
                .map(this::atualizarStatusParaEmAndamentoSeNecessario)
                .toList();
    }

    public void cancelarEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com ID: " + id));
        atualizarStatusParaEmAndamentoSeNecessario(evento);

        if (evento.getStatus() == StatusEventoEnum.FINALIZADO) {
            throw new IllegalStateException("Não é possível cancelar um evento finalizado.");
        }
        if (evento.getStatus() == StatusEventoEnum.CANCELADO) {
            throw new IllegalStateException("O evento já está cancelado.");
        }

        evento.setStatus(StatusEventoEnum.CANCELADO);
        eventoRepository.save(evento);
    }

    public void concluirEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com ID: " + id));
        atualizarStatusParaEmAndamentoSeNecessario(evento);

        if (evento.getStatus() != StatusEventoEnum.EM_ANDAMENTO) {
            throw new IllegalStateException("O evento só pode ser concluído quando estiver EM_ANDAMENTO.");
        }

        evento.setStatus(StatusEventoEnum.FINALIZADO);
        eventoRepository.save(evento);
    }

    public Optional<Evento> procurarPorId(Long id) {
        return eventoRepository.findById(id)
                .map(this::atualizarStatusParaEmAndamentoSeNecessario);
    }

    public List<Evento> procurarTodosOrdenarPorInicio() {
        return eventoRepository.findProximosEventos(statusesAtivos(), PageRequest.of(0, 8)).stream()
                .map(this::atualizarStatusParaEmAndamentoSeNecessario)
                .toList();
    }

    public List<Evento> procurarEventosAguardandoEmissaoCertificado() {
        return eventoRepository.findEventosEncerrados(StatusEventoEnum.EM_ANDAMENTO, PageRequest.of(0, 10));
    }

    public Page<Evento> procurarProximosFiltrados(String busca, Pageable pageable) {
        String termoBusca = (busca == null) ? "" : busca;
        return eventoRepository.findProximosEventosFiltrados(statusesAtivos(), termoBusca, pageable)
                .map(this::atualizarStatusParaEmAndamentoSeNecessario);
    }

    private List<StatusEventoEnum> statusesAtivos() {
        return List.of(StatusEventoEnum.CRIADO, StatusEventoEnum.EM_ANDAMENTO);
    }

    private Evento atualizarStatusParaEmAndamentoSeNecessario(Evento evento) {
        if (evento.getStatus() != StatusEventoEnum.CRIADO) {
            return evento;
        }
        if (!eventoJaComecou(evento)) {
            return evento;
        }

        evento.setStatus(StatusEventoEnum.EM_ANDAMENTO);
        return eventoRepository.save(evento);
    }

    private boolean eventoJaComecou(Evento evento) {
        if (evento.getData() == null || evento.getHorarioInicio() == null || evento.getHorarioInicio().isBlank()) {
            return false;
        }

        try {
            LocalDateTime inicioEvento = LocalDateTime.of(
                    evento.getData().toLocalDate(),
                    LocalTime.parse(evento.getHorarioInicio(), DateTimeFormatter.ofPattern("HH:mm"))
            );
            return !inicioEvento.isAfter(LocalDateTime.now());
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
