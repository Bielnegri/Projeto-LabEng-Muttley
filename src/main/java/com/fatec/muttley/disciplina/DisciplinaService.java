package com.fatec.muttley.disciplina;

import com.fatec.muttley.certificado.CertificadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    @Autowired
    private DisciplinaMapper disciplinaMapper;
    @Autowired
    private CertificadoRepository certificadoRepository;

    public Disciplina salvarOuAtualizar(AtualizacaoDisciplina dto){
        if (dto.id() != null){
            Disciplina existente = disciplinaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada com id: " + dto.id()));
            disciplinaMapper.updateEntityFromDto(dto, existente);
            return disciplinaRepository.save(existente);
        } else {
            Disciplina novaDisciplina = disciplinaMapper.toEntityFromAtualizacao(dto);
            return disciplinaRepository.save(novaDisciplina);
        }
    }

    public List<Disciplina> procurarTodas(){
        return disciplinaRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId(Long id){
        certificadoRepository.deleteById(id);
    }

    public Optional<Disciplina> procurarPorId(Long id){
        return disciplinaRepository.findById(id);
    }
}
