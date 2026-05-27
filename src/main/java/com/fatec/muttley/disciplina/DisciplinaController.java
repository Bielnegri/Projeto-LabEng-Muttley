package com.fatec.muttley.disciplina;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    @GetMapping
    public ResponseEntity<List<Disciplina>> listarTodas() {
        return ResponseEntity.ok(disciplinaService.procurarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoDisciplina> buscarPorId(@PathVariable Long id) {
        Disciplina disciplina = disciplinaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
        return ResponseEntity.ok(disciplinaMapper.toAtualizacaoDto(disciplina));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoDisciplina dto) {
        Disciplina disciplinaSalva = disciplinaService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Disciplina '" + disciplinaSalva.getNome() + "' criada com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoDisciplina dto) {
        disciplinaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
        dto = dto.withId(id);
        Disciplina disciplinaSalva = disciplinaService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Disciplina '" + disciplinaSalva.getNome() + "' atualizada com sucesso."));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        disciplinaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
        disciplinaService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Disciplina " + id + " deletada com sucesso."));
    }
}