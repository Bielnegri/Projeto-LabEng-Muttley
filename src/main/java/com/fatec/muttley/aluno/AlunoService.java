package com.fatec.muttley.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AlunoMapper alunoMapper;

    public Aluno salvarOuAtualizar(AtualizacaoAluno dto){
        if (dto.id() != null){
            Aluno existente = alunoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com id: ." + dto.id()));
            alunoMapper.updateEntityFromDto(dto, existente);
            return  alunoRepository.save(existente);
        } else {
            Aluno novoAluno = alunoMapper.toEntityFromAtualizacao(dto);
            return alunoRepository.save(novoAluno);
        }
    }

    public List<Aluno> procurarTodos(){
        return alunoRepository.findAll(Sort.by("matricula").ascending());
    }

    public void apagarPorId(Long id){
        alunoRepository.deleteById(id);
    }

    public Optional<Aluno> procurarPorId(Long id){
        return alunoRepository.findById(id);
    }
}
