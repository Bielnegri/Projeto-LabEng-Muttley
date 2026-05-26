package com.fatec.muttley.endereco;

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
@RequestMapping("/api/admin/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos() {
        return ResponseEntity.ok(enderecoService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoEndereco> buscarPorId(@PathVariable Long id) {
        Endereco endereco = enderecoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado."));
        return ResponseEntity.ok(enderecoMapper.toAtualizacaoDto(endereco));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoEndereco dto) {
        Endereco enderecoSalvo = enderecoService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Endereço '" + enderecoSalvo.getLogradouro() + "' criado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoEndereco dto) {
        enderecoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado."));
        Endereco enderecoSalvo = enderecoService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Endereço '" + enderecoSalvo.getLogradouro() + "' atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        enderecoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado."));
        enderecoService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Endereço " + id + " foi apagado!"));
    }
}