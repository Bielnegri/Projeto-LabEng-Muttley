package com.fatec.muttley.disciplina;

import com.fatec.muttley.professor.Professor;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisciplinaServiceTest {


    @Mock
    private DisciplinaRepository disciplinaRepository;
    @Mock
    private DisciplinaMapper disciplinaMapper;
    @InjectMocks
    private DisciplinaService disciplinaService;

    @Test
    void DeveCriarDisciplinaQuandoIDForNulo(){
        AtualizacaoDisciplina dto = new AtualizacaoDisciplina(
                null,
                "Programação Orientada a Objetos",
                "Ensino redirecionado a manipulação de objetos em java",
                "Tarde"
        );
        Professor professor = new Professor();
        professor.setId(1L);


        Disciplina salvo = new Disciplina();
        salvo.setId(1L);
        salvo.setNome("Programação Orientada a Objetos");
        salvo.setProfessor(professor);

        Disciplina novo = new Disciplina();
        novo.setNome("Programação Orientada a Objetos");

        when(disciplinaMapper.toEntityFromAtualizacao(dto)).thenReturn(novo);
        when(disciplinaRepository.save(novo)).thenReturn(salvo);

        Disciplina resultado = disciplinaService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Programação Orientada a Objetos");

        verify(disciplinaMapper).toEntityFromAtualizacao(dto);
        verify(disciplinaMapper, never()).updateEntityFromDto(any(), any());
        verify(disciplinaRepository).save(novo);

    }

    @Test
    void DeveAtualizarDisciplinaQuandoIdForInformado(){

        AtualizacaoDisciplina dto = new AtualizacaoDisciplina(
                10L,
                "POO",
                "Ensino redirecionado a manipulação de objetos em java",
                "Noite"
                
        );
        Disciplina existente = new Disciplina();
        existente.setId(10L);
        existente.setNome("disciplina Antigo");

        when(disciplinaRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(disciplinaRepository.save(existente)).thenReturn(existente);

        Disciplina resultado = disciplinaService.salvarOuAtualizar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(disciplinaRepository).findById(10L);
        verify(disciplinaMapper).updateEntityFromDto(dto, existente);
        verify(disciplinaRepository).save(existente);
        verify(disciplinaMapper, never()).toEntityFromAtualizacao(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizardisciplinaInexistente() {
        com.fatec.muttley.disciplina.AtualizacaoDisciplina dto = new com.fatec.muttley.disciplina.AtualizacaoDisciplina(
                99L,
                "Engenharia de Software III",
                "Lero lero",
                "Tarde"
        );

        when(disciplinaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> disciplinaService.salvarOuAtualizar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(disciplinaRepository).findById(99L);
        verify(disciplinaRepository, never()).save(any());
    }

    @Test
    void deveRetornarListaOrdenadaPorNomeAoProcurarTodos() {
        List<Disciplina> disciplinas = List.of(new Disciplina(), new Disciplina());
        Sort sortEsperado = Sort.by("nome").ascending();

        when(disciplinaRepository.findAll(sortEsperado)).thenReturn(disciplinas);

        List<Disciplina> resultado = disciplinaService.procurarTodas();

        assertThat(resultado).hasSize(2);
        verify(disciplinaRepository).findAll(sortEsperado);
    }

    @Test
    void deveProcurardisciplinaPorId() {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(5L);

        when(disciplinaRepository.findById(5L)).thenReturn(Optional.of(disciplina));

        Optional<Disciplina> resultado = disciplinaService.procurarPorId(5L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(disciplinaRepository).findById(5L);
    }

    @Test
    void deveApagardisciplinaPorId() {
        disciplinaService.apagarPorId(7L);

        verify(disciplinaRepository).deleteById(7L);
    }
}
