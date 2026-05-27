package com.fatec.muttley.local;

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
@RequestMapping("/api/admin/locais")
public class LocalController {

    @Autowired
    private LocalService localService;

    @Autowired
    private LocalMapper localMapper;

    @GetMapping
    public ResponseEntity<List<Local>> listarTodos() {
        return ResponseEntity.ok(localService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoLocal> buscarPorId(@PathVariable Long id) {
        Local local = localService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado."));
        return ResponseEntity.ok(localMapper.toAtualizacaoDto(local));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoLocal dto) {
        Local localSalvo = localService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Local '" + localSalvo.getNome() + "' criado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoLocal dto) {
        localService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado."));
        dto = dto.withId(id);
        Local localSalvo = localService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Local '" + localSalvo.getNome() + "' atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        localService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado."));
        localService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Local " + id + " foi apagado!"));
    }
}