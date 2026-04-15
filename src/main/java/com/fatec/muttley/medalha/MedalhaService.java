package com.fatec.muttley.medalha;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.aluno.AlunoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MedalhaService {
    @Autowired
    private MedalhaRepository medalhaRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private MedalhaMapper medalhaMapper;

    public Medalha salvarOuAtualizar(AtualizacaoMedalha dto) {
        // Valida se a aluno existe
        Aluno aluno = alunoService.procurarPorId(dto.alunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrada com ID: " + dto.alunoId()));
        if (dto.id() != null) {
            // atualizando Busca existente e atualiza
            Medalha existente = medalhaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.id()));
            medalhaMapper.updateEntityFromDto(dto, existente);
            existente.setAluno(aluno); // Atualiza a aluno
            return medalhaRepository.save(existente);
        } else {
            // criando Novo caminhão
            Medalha novoMedalha = medalhaMapper.toEntityFromAtualizacao(dto);
            novoMedalha.setAluno(aluno); // Define a aluno completa

            return medalhaRepository.save(novoMedalha);
        }
    }

    public List<Medalha> procurarTodos(){
        return medalhaRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId (Long id) {
        medalhaRepository.deleteById(id);
    }

    public Optional<Medalha> procurarPorId(Long id) {
        return medalhaRepository.findById(id);
    }
}