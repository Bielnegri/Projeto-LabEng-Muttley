package com.fatec.muttley.colaborador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ColaboradorService {
    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private ColaboradorMapper colaboradorMapper;

    public Colaborador salvarOuAtualizar(AtualizacaoColaborador dto){
        if (dto.id() != null){
            Colaborador existente = colaboradorRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com id: ." + dto.id()));
            colaboradorMapper.updateEntityFromDto(dto, existente);
            return  colaboradorRepository.save(existente);
        } else {
            Colaborador novoColaborador = colaboradorMapper.toEntityFromAtualizacao(dto);
            return colaboradorRepository.save(novoColaborador);
        }
    }

    public List<Colaborador> procurarTodos(){
        return colaboradorRepository.findAll(Sort.by("funcao").ascending());
    }

    public void apagarPorId(Long id){
        colaboradorRepository.deleteById(id);
    }

    public Optional<Colaborador> procurarPorId(Long id){
        return colaboradorRepository.findById(id);
    }
}
