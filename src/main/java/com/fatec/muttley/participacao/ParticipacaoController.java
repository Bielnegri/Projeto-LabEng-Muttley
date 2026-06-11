package com.fatec.muttley.participacao;

import com.fatec.muttley.email.EmailProducer;
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
@RequestMapping("/api/participacoes")
public class ParticipacaoController {

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private ParticipacaoMapper participacaoMapper;

    @Autowired
    private EmailProducer emailProducer;

    @GetMapping
    public ResponseEntity<List<ParticipacaoComEventoResponse>> listarTodos() {
        List<ParticipacaoComEventoResponse> participacoes = participacaoService.procurarTodos().stream()
                .map(ParticipacaoComEventoResponse::from)
                .toList();
        return ResponseEntity.ok(participacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipacaoComEventoResponse> buscarPorId(@PathVariable Long id) {
        Participacao participacao = participacaoService.procurarPorIdComDados(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
        return ResponseEntity.ok(ParticipacaoComEventoResponse.from(participacao));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoParticipacao dto) {
        Participacao participacaoSalva = participacaoService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Participação '" + participacaoSalva.getInscricao() + "' criada com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoParticipacao dto) {
        participacaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
        dto = dto.withId(id);
        Participacao participacaoSalva = participacaoService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Participação '" + participacaoSalva.getInscricao() + "' alterada com sucesso."));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        participacaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
        participacaoService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Participação " + id + " deletada com sucesso."));
    }
}
