package com.fatec.muttley.professor;

import com.fatec.muttley.aluno.Aluno;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    public Professor salvarOuAtualizar(AtualizacaoProfessor dto){
        if (dto.id() != null) {
            // atualizando Busca existente e atualiza
            Professor existente = professorRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com ID: " + dto.id()));
            professorMapper.updateEntityFromDto(dto, existente);
            return professorRepository.save(existente);
        } else {
            // criando Novo professor
            Professor novoProfessor = professorMapper.toEntityFromAtualizacao(dto);
            return professorRepository.save(novoProfessor);
        }
    }

    public List<Professor> procurarTodos(){
        return professorRepository.findAll(Sort.by("nome").ascending());
    }
    public void apagarPorId (Long id) {
        professorRepository.deleteById(id);
    }

    public Optional<Professor> procurarPorId(Long id) {
        return professorRepository.findById(id);
    }
}
