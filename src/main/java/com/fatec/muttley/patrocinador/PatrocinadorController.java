package com.fatec.muttley.patrocinador;

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
@RequestMapping("/api/admin/patrocinadores")
public class PatrocinadorController {

    @Autowired
    private PatrocinadorService patrocinadorService;

    @Autowired
    private PatrocinadorMapper patrocinadorMapper;

    @GetMapping
    public ResponseEntity<List<Patrocinador>> listarTodos() {
        return ResponseEntity.ok(patrocinadorService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoPatrocinador> buscarPorId(@PathVariable Long id) {
        Patrocinador patrocinador = patrocinadorService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado."));
        return ResponseEntity.ok(patrocinadorMapper.toAtualizacaoDto(patrocinador));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoPatrocinador dto) {
        Patrocinador patrocinadorSalvo = patrocinadorService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Patrocinador '" + patrocinadorSalvo.getNome() + "' criado com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoPatrocinador dto) {
        patrocinadorService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado."));
        dto = dto.withId(id);
        Patrocinador patrocinadorSalvo = patrocinadorService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Patrocinador '" + patrocinadorSalvo.getNome() + "' atualizado com sucesso."));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        patrocinadorService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado."));
        patrocinadorService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Patrocinador " + id + " deletado com sucesso."));
    }
}