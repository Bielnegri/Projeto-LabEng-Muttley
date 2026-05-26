package com.fatec.muttley.local;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fatec.muttley.endereco.Endereco;
import com.fatec.muttley.endereco.EnderecoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LocalService {
    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private LocalMapper localMapper;

    public Local salvarOuAtualizar(AtualizacaoLocal dto) {
        // Valida se a endereco existe
        Endereco endereco = enderecoService.procurarPorId(dto.enderecoId())
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado com ID: " + dto.enderecoId()));
        if (dto.id() != null) {
            // atualizando Busca existente e atualiza
            Local existente = localRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Local não encontrado com ID: " + dto.id()));
            localMapper.updateEntityFromDto(dto, existente);
            existente.setEndereco(endereco); // Atualiza a endereco
            return localRepository.save(existente);
        } else {
            // criando Novo local
            Local novoLocal = localMapper.toEntityFromAtualizacao(dto);
            novoLocal.setEndereco(endereco); // Define a endereco completa

            return localRepository.save(novoLocal);
        }
    }

    public List<Local> procurarTodos(){
        return localRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId (Long id) {
        localRepository.deleteById(id);
    }

    public Optional<Local> procurarPorId(Long id) {
        return localRepository.findById(id);
    }
}