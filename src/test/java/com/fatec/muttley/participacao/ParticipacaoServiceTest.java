package com.fatec.muttley.participacao;

import com.fatec.muttley.aluno.Aluno;
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
class ParticipacaoServiceTest {

    @Mock
    private ParticipacaoRepository participacaoRepository;

    @Mock
    private ParticipacaoMapper participacaoMapper;

    @Mock
    private AlunoService alunoService;

    @Mock
    private EventoService eventoService;

    @InjectMocks
    private ParticipacaoService participacaoService;

    @Test
    void deveCriarParticipanteComAlunoEEvento() {
        AtualizacaoParticipacao dto = new AtualizacaoParticipacao(null, 101, 1L, 2L);
        Participacao novo = new Participacao();
        novo.setInscricao(101);

        Aluno aluno = new Aluno();
        aluno.setId(1L);
        Evento evento = new Evento();
        evento.setId(2L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(eventoService.procurarPorId(2L)).thenReturn(Optional.of(evento));
        when(participacaoMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(participacaoRepository.save(novo)).thenReturn(novo);

        Participacao resultado = participacaoService.salvarOuAtualizar(dto);

        assertThat(resultado.getAluno()).isEqualTo(aluno);
        assertThat(resultado.getEvento()).isEqualTo(evento);
        verify(participacaoRepository).save(novo);
    }

    @Test
    void deveAtualizarParticipanteComAlunoEEvento() {
        AtualizacaoParticipacao dto = new AtualizacaoParticipacao(10L, 202, 3L, 4L);
        Participacao existente = new Participacao();
        existente.setId(10L);
        existente.setInscricao(200);

        Aluno aluno = new Aluno();
        aluno.setId(3L);
        Evento evento = new Evento();
        evento.setId(4L);

        when(alunoService.procurarPorId(3L)).thenReturn(Optional.of(aluno));
        when(eventoService.procurarPorId(4L)).thenReturn(Optional.of(evento));
        when(participacaoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(participacaoRepository.save(existente)).thenReturn(existente);

        Participacao resultado = participacaoService.salvarOuAtualizar(dto);

        assertThat(resultado.getAluno()).isEqualTo(aluno);
        assertThat(resultado.getEvento()).isEqualTo(evento);
        verify(participacaoMapper).updateEntityFromDto(dto, existente);
        verify(participacaoRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoQuandoAlunoNaoEncontrado() {
        AtualizacaoParticipacao dto = new AtualizacaoParticipacao(null, 101, 99L, 2L);
        when(alunoService.procurarPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> participacaoService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Aluno");

        verify(eventoService, never()).procurarPorId(2L);
        verify(participacaoRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoEncontrado() {
        AtualizacaoParticipacao dto = new AtualizacaoParticipacao(null, 101, 1L, 98L);
        Aluno aluno = new Aluno();
        aluno.setId(1L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(eventoService.procurarPorId(98L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> participacaoService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Evento");

        verify(participacaoRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
