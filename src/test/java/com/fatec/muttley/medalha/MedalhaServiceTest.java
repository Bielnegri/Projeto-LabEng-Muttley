package com.fatec.muttley.medalha;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.aluno.AlunoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class MedalhaServiceTest {

    @Mock
    private MedalhaRepository medalhaRepository;

    @Mock
    private MedalhaMapper medalhaMapper;

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private MedalhaService medalhaService;

    @Test
    void deveCriarMedalhaQuandoIdForNulo() {
        AtualizacaoMedalha dto = new AtualizacaoMedalha(
                null,
                "Eng. de Software",
                "Recebida ao participar do evento de Eng. de Software",
                1L
        );
        Medalha novo = new Medalha();
        novo.setNome("Eng. de Software");
        Medalha salvo = new Medalha();
        salvo.setId(1L);
        salvo.setNome("Eng. de Software");

        Aluno aluno = new Aluno();
        aluno.setId(1L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(medalhaMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(medalhaRepository.save(novo)).thenReturn(salvo);

        Medalha resultado = medalhaService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Eng. de Software");
        verify(medalhaMapper).toEntityFromAtualizacao(dto);
        verify(medalhaMapper, never()).updateEntityFromDto(any(), any());
        verify(medalhaRepository).save(novo);
    }

    @Test
    void deveAtualizarMedalhaQuandoIdForInformado() {
        AtualizacaoMedalha dto = new AtualizacaoMedalha(
                10L,
                "Eng. de Software",
                "Recebida ao participar do evento de Eng. de Software",
                1L
        );
        Medalha existente = new Medalha();
        existente.setId(10L);
        existente.setNome("Eng. de Software");

        Aluno aluno = new Aluno();
        aluno.setId(1L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(medalhaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(medalhaRepository.save(existente)).thenReturn(existente);

        Medalha resultado = medalhaService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(medalhaRepository).findById(10L);
        verify(medalhaMapper).updateEntityFromDto(dto, existente);
        verify(medalhaRepository).save(existente);
        verify(medalhaMapper, never()).toEntityFromAtualizacao(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarMedalhaInexistente() {
        AtualizacaoMedalha dto = new AtualizacaoMedalha(
                99L,
                "Eng. de Software",
                "Recebida ao participar do evento de Eng. de Software",
                1L
        );

        Aluno aluno = new Aluno();
        aluno.setId(1L);

        when(alunoService.procurarPorId(1L)).thenReturn(Optional.of(aluno));
        when(medalhaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medalhaService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(medalhaRepository).findById(99L);
        verify(medalhaRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaOrdenadaPorNomeAoProcurarTodos() {
        List<Medalha> medalhas = List.of(new Medalha(), new Medalha());
        Sort sortEsperado = Sort.by("nome").ascending();

        when(medalhaRepository.findAll(sortEsperado)).thenReturn(medalhas);

        List<Medalha> resultado = medalhaService.procurarTodos();

        assertThat(resultado).hasSize(2);
        verify(medalhaRepository).findAll(sortEsperado);
    }

    @Test
    void deveProcurarMedalhaPorId() {
        Medalha medalha = new Medalha();
        medalha.setId(5L);

        when(medalhaRepository.findById(5L)).thenReturn(Optional.of(medalha));

        Optional<Medalha> resultado = medalhaService.procurarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(medalhaRepository).findById(5L);
    }

    @Test
    void deveApagarMedalhaPorId() {
        medalhaService.apagarPorId(7L);

        verify(medalhaRepository).deleteById(7L);
    }
}
