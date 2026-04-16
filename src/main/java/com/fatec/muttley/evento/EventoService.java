package com.fatec.muttley.evento;

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

    public Evento salvarOuAtualizar(AtualizacaoEvento dto) {
        if (dto.id() != null) {
            Evento existente = eventoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("O evento: : " + dto.id() + " não existe."));
            eventoMapper.updateEntityFromDto(dto, existente);
            return eventoRepository.save(existente);
        } else {
            Evento novoEvento = eventoMapper.toEntityFromAtualizacao(dto);
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