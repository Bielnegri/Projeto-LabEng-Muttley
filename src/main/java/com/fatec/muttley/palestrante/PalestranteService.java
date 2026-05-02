package com.fatec.muttley.palestrante;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PalestranteService {
    @Autowired
    private PalestranteRepository palestranteRepository;

    @Autowired
    private PalestranteMapper palestranteMapper;

    public Palestrante salvarOuAtualizar(AtualizacaoPalestrante dto){
        if (dto.id() != null){
            Palestrante existente = palestranteRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Palestrante não encontrado com id: ." + dto.id()));
            palestranteMapper.updateEntityFromDto(dto, existente);
            return  palestranteRepository.save(existente);
        } else {
            Palestrante novoPalestrante = palestranteMapper.toEntityFromAtualizacao(dto);
            return palestranteRepository.save(novoPalestrante);
        }
    }

    public List<Palestrante> procurarTodos(){
        return palestranteRepository.findAll(Sort.by("matricula").ascending());
    }

    public void apagarPorId(Long id){
        palestranteRepository.deleteById(id);
    }

    public Optional<Palestrante> procurarPorId(Long id){
        return palestranteRepository.findById(id);
    }
}
