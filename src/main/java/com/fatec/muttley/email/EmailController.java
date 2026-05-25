package com.fatec.muttley.email;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private EventoService eventoService;

    @PostMapping("/confirmacao-cadastro/{participacaoId}")
    public ResponseEntity<Map<String, String>> notificarConfirmacaoCadastro(@PathVariable Long participacaoId) {
        try {
            Participacao participacao = participacaoService.procurarPorId(participacaoId)
                    .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));

            emailService.enviarConfirmacaoCadastro(participacao);
            return ResponseEntity.ok(Map.of("message", "E-mail de confirmação enviado."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/evento-cancelado/{eventoId}")
    public ResponseEntity<Map<String, String>> notificarEventoCancelado(@PathVariable Long eventoId) {
        try {
            Evento evento = eventoService.procurarPorId(eventoId)
                    .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

            emailService.enviarEventoCancelado(evento);
            return ResponseEntity.ok(Map.of("message", "Notificações de cancelamento enviadas com sucesso."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/evento-concluido/{eventoId}")
    public ResponseEntity<Map<String, String>> notificarEventoConcluido(@PathVariable Long eventoId) {
        try {
            Evento evento = eventoService.procurarPorId(eventoId)
                    .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

            emailService.enviarEventoConcluido(evento);
            return ResponseEntity.ok(Map.of("message", "Notificações de conclusão enviadas com sucesso."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}