package com.fatec.muttley.aluno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private AlunoMapper alunoMapper;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    void deveCriarAlunoQuandoIdForNulo() {
        AtualizacaoAluno dto = new AtualizacaoAluno(
                null,
                "Ana",
                "ana@email.com",
                "11999999999",
                "FATEC",
                "12345"
        );
        Aluno novo = new Aluno();
        novo.setNome("Ana");
        Aluno salvo = new Aluno();
        salvo.setId(1L);
        salvo.setNome("Ana");

        when(alunoMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(alunoRepository.save(novo)).thenReturn(salvo);

        Aluno resultado = alunoService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Ana");
        verify(alunoMapper).toEntityFromAtualizacao(dto);
        verify(alunoMapper, never()).updateEntityFromDto(any(), any());
        verify(alunoRepository).save(novo);
    }

    @Test
    void deveAtualizarAlunoQuandoIdForInformado() {
        AtualizacaoAluno dto = new AtualizacaoAluno(
                10L,
                "Bruno Atualizado",
                "bruno@email.com",
                "11888888888",
                "FATEC",
                "54321"
        );
        Aluno existente = new Aluno();
        existente.setId(10L);
        existente.setNome("Bruno");

        when(alunoRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(alunoRepository.save(existente)).thenReturn(existente);

        Aluno resultado = alunoService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(alunoRepository).findById(10L);
        verify(alunoMapper).updateEntityFromDto(dto, existente);
        verify(alunoRepository).save(existente);
        verify(alunoMapper, never()).toEntityFromAtualizacao(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarAlunoInexistente() {
        AtualizacaoAluno dto = new AtualizacaoAluno(
                99L,
                "Carlos",
                "carlos@email.com",
                "11777777777",
                "FATEC",
                "99999"
        );

        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alunoService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(alunoRepository).findById(99L);
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaOrdenadaPorNomeAoProcurarTodos() {
        List<Aluno> alunos = List.of(new Aluno(), new Aluno());
        Sort sortEsperado = Sort.by("nome").ascending();

        when(alunoRepository.findAll(sortEsperado)).thenReturn(alunos);

        List<Aluno> resultado = alunoService.procurarTodos();

        assertThat(resultado).hasSize(2);
        verify(alunoRepository).findAll(sortEsperado);
    }

    @Test
    void deveProcurarAlunoPorId() {
        Aluno aluno = new Aluno();
        aluno.setId(5L);

        when(alunoRepository.findById(5L)).thenReturn(Optional.of(aluno));

        Optional<Aluno> resultado = alunoService.procurarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(alunoRepository).findById(5L);
    }

    @Test
    void deveApagarAlunoPorId() {
        alunoService.apagarPorId(7L);

        verify(alunoRepository).deleteById(7L);
    }
}
