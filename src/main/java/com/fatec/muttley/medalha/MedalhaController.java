package com.fatec.muttley.medalha;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/medalhas")
public class MedalhaController {

    @Autowired
    private MedalhaService medalhaService;

    @Autowired
    private MedalhaMapper medalhaMapper;

    @GetMapping
    public ResponseEntity<List<Medalha>> listarTodos() {
        return ResponseEntity.ok(medalhaService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoMedalha> buscarPorId(@PathVariable Long id) {
        Medalha medalha = medalhaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada."));
        return ResponseEntity.ok(medalhaMapper.toAtualizacaoDto(medalha));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoMedalha dto) {
        Medalha medalhaSalva = medalhaService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Medalha '" + medalhaSalva.getNome() + "' criada com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoMedalha dto) {
        medalhaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada."));
        dto = dto.withId(id);
        Medalha medalhaSalva = medalhaService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Medalha '" + medalhaSalva.getNome() + "' atualizada com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        medalhaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada."));
        medalhaService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Medalha " + id + " foi apagada!"));
    }
}