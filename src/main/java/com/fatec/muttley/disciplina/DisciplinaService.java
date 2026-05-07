package com.fatec.muttley.disciplina;

import com.fatec.muttley.professor.Professor;
import com.fatec.muttley.professor.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    public Disciplina salvarOuAtualizar(AtualizacaoDisciplina dto) {
        Professor professor = null;
        if (dto.id_professor() != null) {
            professor = professorRepository.findById(dto.id_professor())
                    .orElseThrow(() -> new EntityNotFoundException("Professor nao encontrado com id: " + dto.id_professor()));
        }

        Disciplina disciplina;
        if (dto.id() != null) {
            disciplina = disciplinaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina nao encontrada com id: " + dto.id()));
            disciplinaMapper.updateEntityFromDto(dto, disciplina);
        } else {
            disciplina = disciplinaMapper.toEntityFromAtualizacao(dto);
        }

        disciplina.setProfessor(professor);
        return disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> procurarTodas() {
        return disciplinaRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId(Long id) {
        disciplinaRepository.deleteById(id);
    }

    public Optional<Disciplina> procurarPorId(Long id) {
        return disciplinaRepository.findById(id);
    }
}
