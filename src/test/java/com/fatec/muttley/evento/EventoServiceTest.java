package com.fatec.muttley.evento;

import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.disciplina.DisciplinaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private EventoMapper eventoMapper;

    @Mock
    private DisciplinaService disciplinaService;

    @InjectMocks
    private EventoService eventoService;

    @Test
    void deveCriarEventoQuandoIdForNulo() {
        AtualizacaoEvento dto = new AtualizacaoEvento(
                null,
                "Semana de Tecnologia",
                "Auditorio",
                Date.valueOf("2026-06-20"),
                "19:00",
                1L
        );
        Evento novo = new Evento();
        novo.setTema("Semana de Tecnologia");
        Evento salvo = new Evento();
        salvo.setId(1L);
        salvo.setTema("Semana de Tecnologia");
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);

        when(disciplinaService.procurarPorId(1L)).thenReturn(Optional.of(disciplina));
        when(eventoMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(eventoRepository.save(novo)).thenReturn(salvo);

        Evento resultado = eventoService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTema()).isEqualTo("Semana de Tecnologia");
        verify(eventoMapper).toEntityFromAtualizacao(dto);
        verify(eventoMapper, never()).updateEntityFromDto(any(), any());
        verify(eventoRepository).save(novo);
        assertThat(novo.getDisciplina()).isEqualTo(disciplina);
    }

    @Test
    void deveAtualizarEventoQuandoIdForInformado() {
        AtualizacaoEvento dto = new AtualizacaoEvento(
                10L,
                "Semana de Inovacao",
                "Laboratorio",
                Date.valueOf("2026-06-21"),
                "20:00",
                2L
        );
        Evento existente = new Evento();
        existente.setId(10L);
        existente.setTema("Evento Antigo");
        Disciplina disciplina = new Disciplina();
        disciplina.setId(2L);

        when(disciplinaService.procurarPorId(2L)).thenReturn(Optional.of(disciplina));
        when(eventoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(eventoRepository.save(existente)).thenReturn(existente);

        Evento resultado = eventoService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(eventoRepository).findById(10L);
        verify(eventoMapper).updateEntityFromDto(dto, existente);
        verify(eventoRepository).save(existente);
        verify(eventoMapper, never()).toEntityFromAtualizacao(any());
        assertThat(existente.getDisciplina()).isEqualTo(disciplina);
    }

    @Test
    void deveLancarExcecaoAoAtualizarEventoInexistente() {
        AtualizacaoEvento dto = new AtualizacaoEvento(
                99L,
                "Hackathon",
                "Bloco B",
                Date.valueOf("2026-07-01"),
                "18:30",
                null
        );

        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventoService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(eventoRepository).findById(99L);
        verify(eventoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoDisciplinaNaoExiste() {
        AtualizacaoEvento dto = new AtualizacaoEvento(
                null,
                "Evento sem disciplina valida",
                "Bloco C",
                Date.valueOf("2026-08-02"),
                "08:00",
                999L
        );

        when(disciplinaService.procurarPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventoService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Disciplina");

        verify(eventoRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaOrdenadaPorTemaAoProcurarTodos() {
        List<Evento> eventos = List.of(new Evento(), new Evento());
        Sort sortEsperado = Sort.by("tema").ascending();

        when(eventoRepository.findAll(sortEsperado)).thenReturn(eventos);

        List<Evento> resultado = eventoService.procurarTodos();

        assertThat(resultado).hasSize(2);
        verify(eventoRepository).findAll(sortEsperado);
    }

    @Test
    void deveProcurarEventoPorId() {
        Evento evento = new Evento();
        evento.setId(5L);

        when(eventoRepository.findById(5L)).thenReturn(Optional.of(evento));

        Optional<Evento> resultado = eventoService.procurarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(eventoRepository).findById(5L);
    }

    @Test
    void deveApagarEventoPorId() {
        eventoService.apagarPorId(7L);

        verify(eventoRepository).deleteById(7L);
    }
}
