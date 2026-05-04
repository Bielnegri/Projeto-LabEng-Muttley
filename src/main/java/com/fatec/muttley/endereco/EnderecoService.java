package com.fatec.muttley.endereco;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    public Endereco salvarOuAtualizar(AtualizacaoEndereco dto){
        if (dto.id() != null){
            Endereco existente = enderecoRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado com id: ." + dto.id()));
            enderecoMapper.updateEntityFromDto(dto, existente);
            return  enderecoRepository.save(existente);
        } else {
            Endereco novoEndereco = enderecoMapper.toEntityFromAtualizacao(dto);
            return enderecoRepository.save(novoEndereco);
        }
    }

    public List<Endereco> procurarTodos(){
        return enderecoRepository.findAll(Sort.by("cidade").ascending());
    }

    public void apagarPorId(Long id){
        enderecoRepository.deleteById(id);
    }

    public Optional<Endereco> procurarPorId(Long id){
        return enderecoRepository.findById(id);
    }
}
