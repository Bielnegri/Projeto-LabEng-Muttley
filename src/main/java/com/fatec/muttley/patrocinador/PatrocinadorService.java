package com.fatec.muttley.patrocinador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PatrocinadorService {
    @Autowired
    private PatrocinadorRepository patrocinadorRepository;

    @Autowired
    private PatrocinadorMapper patrocinadorMapper;

    public Patrocinador salvarOuAtualizar(AtualizacaoPatrocinador dto){
        if (dto.id() != null){
            Patrocinador existente = patrocinadorRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado com id: ." + dto.id()));
            patrocinadorMapper.updateEntityFromDto(dto, existente);
            return  patrocinadorRepository.save(existente);
        } else {
            Patrocinador novoPatrocinador = patrocinadorMapper.toEntityFromAtualizacao(dto);
            return patrocinadorRepository.save(novoPatrocinador);
        }
    }

    public List<Patrocinador> procurarTodos(){
        return patrocinadorRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId(Long id){
        patrocinadorRepository.deleteById(id);
    }

    public Optional<Patrocinador> procurarPorId(Long id){
        return patrocinadorRepository.findById(id);
    }
}
