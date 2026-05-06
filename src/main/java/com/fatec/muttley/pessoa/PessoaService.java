package com.fatec.muttley.pessoa;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    public Pessoa salvarOuAtualizar(AtualizacaoPessoa dto){
        if (dto.id() != null) {
            // atualizando Busca existente e atualiza
            Pessoa existente = pessoaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + dto.id()));
            pessoaMapper.updateEntityFromDto(dto, existente);
            return pessoaRepository.save(existente);
        } else {
            // criando Nova pessoa
            Pessoa novaPessoa = pessoaMapper.toEntityFromAtualizacao(dto);
            return pessoaRepository.save(novaPessoa);
        }
    }

    public List<Pessoa> procurarTodos(){
        return pessoaRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId (Long id) {
        pessoaRepository.deleteById(id);
    }

    public Optional<Pessoa> procurarPorId(Long id) {
        return pessoaRepository.findById(id);
    }
}

