package com.fatec.muttley.professor;

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
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private ProfessorMapper professorMapper;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    void deveCriarProfessorQuandoIdForNulo() {
        AtualizacaoProfessor dto = new AtualizacaoProfessor(
                null,
                "Joao",
                "joao@email.com",
                "Mestrado"
        );
        Professor novo = new Professor();
        novo.setNome("Joao");
        Professor salvo = new Professor();
        salvo.setId(1L);
        salvo.setNome("Joao");

        when(professorMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(professorRepository.save(novo)).thenReturn(salvo);

        Professor resultado = professorService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Joao");
        verify(professorMapper).toEntityFromAtualizacao(dto);
        verify(professorMapper, never()).updateEntityFromDto(any(), any());
        verify(professorRepository).save(novo);
    }

    @Test
    void deveAtualizarProfessorQuandoIdForInformado() {
        AtualizacaoProfessor dto = new AtualizacaoProfessor(
                10L,
                "Maria Atualizada",
                "maria@email.com",
                "Doutorado"
        );
        Professor existente = new Professor();
        existente.setId(10L);
        existente.setNome("Maria");

        when(professorRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(professorRepository.save(existente)).thenReturn(existente);

        Professor resultado = professorService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(professorRepository).findById(10L);
        verify(professorMapper).updateEntityFromDto(dto, existente);
        verify(professorRepository).save(existente);
        verify(professorMapper, never()).toEntityFromAtualizacao(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarProfessorInexistente() {
        AtualizacaoProfessor dto = new AtualizacaoProfessor(
                99L,
                "Carlos",
                "carlos@email.com",
                "Especializacao"
        );

        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(professorRepository).findById(99L);
        verify(professorRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaOrdenadaPorNomeAoProcurarTodos() {
        List<Professor> professores = List.of(new Professor(), new Professor());
        Sort sortEsperado = Sort.by("nome").ascending();

        when(professorRepository.findAll(sortEsperado)).thenReturn(professores);

        List<Professor> resultado = professorService.procurarTodos();

        assertThat(resultado).hasSize(2);
        verify(professorRepository).findAll(sortEsperado);
    }

    @Test
    void deveProcurarProfessorPorId() {
        Professor professor = new Professor();
        professor.setId(5L);

        when(professorRepository.findById(5L)).thenReturn(Optional.of(professor));

        Optional<Professor> resultado = professorService.procurarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(professorRepository).findById(5L);
    }

    @Test
    void deveApagarProfessorPorId() {
        professorService.apagarPorId(7L);

        verify(professorRepository).deleteById(7L);
    }
}
