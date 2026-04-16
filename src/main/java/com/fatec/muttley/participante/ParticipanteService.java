package com.fatec.muttley.participante;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private ParticipanteMapper participanteMapper;

    public Participante salvarOuAtualizar(AtualizacaoParticipante dto) {
        if (dto.id() != null) {
            Participante existente = participanteRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado participante com o id: " + dto.id()));
            participanteMapper.updateEntityFromDto(dto, existente);
            return participanteRepository.save(existente);
        } else {
            Participante novoParticipante = participanteMapper.toEntityFromAtualizacao(dto);
            return participanteRepository.save(novoParticipante);
        }
    }

    public List<Participante> procurarTodos() {
        return participanteRepository.findAll(Sort.by("inscricao").ascending());
    }

    public void apagarPorId(Long id) {
        participanteRepository.deleteById(id);
    }

    public Optional<Participante> procurarPorId(Long id) {
        return participanteRepository.findById(id);
    }
}