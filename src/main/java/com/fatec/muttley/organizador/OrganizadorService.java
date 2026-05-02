package com.fatec.muttley.organizador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrganizadorService {
    @Autowired
    private OrganizadorRepository organizadorRepository;

    @Autowired
    private OrganizadorMapper organizadorMapper;

    public Organizador salvarOuAtualizar(AtualizacaoOrganizador dto){
        if (dto.id() != null){
            Organizador existente = organizadorRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Organizador não encontrado com id: ." + dto.id()));
            organizadorMapper.updateEntityFromDto(dto, existente);
            return  organizadorRepository.save(existente);
        } else {
            Organizador novoOrganizador = organizadorMapper.toEntityFromAtualizacao(dto);
            return organizadorRepository.save(novoOrganizador);
        }
    }

    public List<Organizador> procurarTodos(){
        return organizadorRepository.findAll(Sort.by("instituicao").ascending());
    }

    public void apagarPorId(Long id){
        organizadorRepository.deleteById(id);
    }

    public Optional<Organizador> procurarPorId(Long id){
        return organizadorRepository.findById(id);
    }
}
