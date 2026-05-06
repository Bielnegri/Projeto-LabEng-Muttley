package com.fatec.muttley.participante;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.aluno.AlunoService;
import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.EventoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipanteServiceTest {

    @Mock
    private ParticipanteRepository participanteRepository;

    @Mock
    private ParticipanteMapper participanteMapper;

    @Mock
    private AlunoService alunoService;

    @Mock
    private EventoService eventoService;

    @InjectMocks
    private ParticipanteService participanteService;

    @Test
    void deveCriarParticipanteComAlunoEEvento() {
        AtualizacaoParticipante dto = new AtualizacaoParticipante(null, 101, 1L, 2L);
        Participante novo = new Participante();
        novo.setInscricao(101);

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        Evento evento = new Evento();
        evento.setId(2L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(eventoService.procurarPorId(2L)).thenReturn(Optional.of(evento));
        when(participanteMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(participanteRepository.save(novo)).thenReturn(novo);

        Participante resultado = participanteService.salvarOuAtualizar(dto);

        assertThat(resultado.getAluno()).isEqualTo(aluno);
        assertThat(resultado.getEvento()).isEqualTo(evento);
        verify(participanteRepository).save(novo);
    }

    @Test
    void deveAtualizarParticipanteComAlunoEEvento() {
        AtualizacaoParticipante dto = new AtualizacaoParticipante(10L, 202, 3L, 4L);
        Participante existente = new Participante();
        existente.setId(10L);
        existente.setInscricao(200);

        Aluno aluno = new Aluno();
        aluno.setId(3L);
        Evento evento = new Evento();
        evento.setId(4L);

        when(alunoService.procurarPorId(3L)).thenReturn(Optional.of(aluno));
        when(eventoService.procurarPorId(4L)).thenReturn(Optional.of(evento));
        when(participanteRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(participanteRepository.save(existente)).thenReturn(existente);

        Participante resultado = participanteService.salvarOuAtualizar(dto);

        assertThat(resultado.getAluno()).isEqualTo(aluno);
        assertThat(resultado.getEvento()).isEqualTo(evento);
        verify(participanteMapper).updateEntityFromDto(dto, existente);
        verify(participanteRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontrado() {
        AtualizacaoParticipante dto = new AtualizacaoParticipante(null, 101, 99L, 2L);
        when(alunoService.procurarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> participanteService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Aluno");

        verify(eventoService, never()).procurarPorId(2L);
        verify(participanteRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoEncontrado() {
        AtualizacaoParticipante dto = new AtualizacaoParticipante(null, 101, 1L, 98L);
        Aluno aluno = new Aluno();
        aluno.setId(1L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(eventoService.procurarPorId(98L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> participanteService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Evento");

        verify(participanteRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
