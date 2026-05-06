package com.fatec.muttley.professor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ProfessorMapper professorMapper;

    public Professor salvarOuAtualizar(AtualizacaoProfessor dto){
        if (dto.id() != null){
            Professor existente = professorRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com id: ." + dto.id()));
            professorMapper.updateEntityFromDto(dto, existente);
            return  professorRepository.save(existente);
        } else {
            Professor novoProfessor = professorMapper.toEntityFromAtualizacao(dto);
            return professorRepository.save(novoProfessor);
        }
    }

    public List<Professor> procurarTodos(){
        return professorRepository.findAll(Sort.by("titulacao").ascending());
    }

    public void apagarPorId(Long id){
        professorRepository.deleteById(id);
    }

    public Optional<Professor> procurarPorId(Long id){
        return professorRepository.findById(id);
    }
}
