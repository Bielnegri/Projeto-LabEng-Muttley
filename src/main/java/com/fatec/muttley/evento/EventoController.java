package com.fatec.muttley.evento;

import com.fatec.muttley.certificado.Certificado;
import com.fatec.muttley.certificado.CertificadoService;
import com.fatec.muttley.email.EmailProducer;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.participacao.AtualizacaoParticipacao;
import com.fatec.muttley.participacao.AtualizacaoParticipacaoNovoEvento;
import com.fatec.muttley.participacao.InscricaoPublicaRequest;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoComEventoResponse;
import com.fatec.muttley.participacao.ParticipacaoService;
import com.fatec.muttley.qrcode.QrCodeClient;
import com.fatec.muttley.qrcode.QrCodeProducer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoMapper eventoMapper;

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private QrCodeProducer qrCodeProducer;

    @Autowired
    private QrCodeClient qrCodeClient;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private EmailProducer emailProducer;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @GetMapping("/api/eventos")
    public ResponseEntity<List<EventoPublicoResponse>> listarEventosPublicos() {
        List<EventoPublicoResponse> eventos = eventoService.procurarDisponiveisParaInscricao().stream()
                .map(evento -> EventoPublicoResponse.from(evento, inscricoesEncerradas(evento)))
                .toList();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/api/eventos/{id}")
    public ResponseEntity<EventoPublicoResponse> buscarEventoPublico(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
        return ResponseEntity.ok(EventoPublicoResponse.from(evento, inscricoesEncerradas(evento)));
    }

    @PostMapping("/api/eventos/{id}/inscricoes")
    public ResponseEntity<Map<String, Object>> registrarInscricaoPublica(
            @PathVariable Long id,
            @RequestBody @Valid InscricaoPublicaRequest dados) {
        Participacao participacao = participacaoService.registrarInscricaoPublica(id, dados);
        emailProducer.publicarConfirmacaoCadastro(participacao);
        emailProducer.publicarCredenciaisLogin(participacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Inscricao realizada com sucesso.",
                "participacaoId", participacao.getId(),
                "inscricao", participacao.getInscricao()
        ));
    }

    @GetMapping("/api/admin/eventos")
    public ResponseEntity<Page<Evento>> listarEventos(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "data") String ordenar,
            @RequestParam(required = false) StatusEventoEnum status,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {

        Sort sort = ordenar.equals("tema")
                ? Sort.by("tema").ascending()
                : Sort.by("data").ascending().and(Sort.by("horarioInicio").ascending());

        Pageable pageable = PageRequest.of(pagina, tamanho, sort);
        return ResponseEntity.ok(eventoService.procurarProximosFiltrados(busca, status, pageable));
    }

    @GetMapping("/api/admin/eventos/{id}")
    public ResponseEntity<AtualizacaoEvento> buscarPorId(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));
        return ResponseEntity.ok(eventoMapper.toAtualizacaoDto(evento));
    }

    @PostMapping("/api/admin/eventos")
    public ResponseEntity<Map<String, Object>> criar(@RequestBody @Valid EventoComParticipacaoDTO dto) {
        AtualizacaoEvento dtoEvento = dto.evento();
        List<AtualizacaoParticipacaoNovoEvento> dtoNovasParticipacoes = dto.participacoes();

        Evento eventoSalvo = eventoService.salvarOuAtualizar(dtoEvento);

        if(dtoNovasParticipacoes != null) {
            for (AtualizacaoParticipacaoNovoEvento participacao : dtoNovasParticipacoes) {
                AtualizacaoParticipacao dtoParticipacao = new AtualizacaoParticipacao(
                        participacao.id(),
                        participacao.inscricao(),
                        participacao.tipo(),
                        participacao.pessoaId(),
                        eventoSalvo.getId()
                );

                participacaoService.salvarOuAtualizar(dtoParticipacao);
            }
        }

        qrCodeProducer.publicarQrCodeInscricao(eventoSalvo, frontendUrl);
        qrCodeProducer.publicarQrCodeConfirmacao(eventoSalvo, frontendUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Evento '" + eventoSalvo.getTema() + "' criado com sucesso.",
                "id", eventoSalvo.getId()
        ));
    }

    @PutMapping("/api/admin/eventos/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid EventoComParticipacaoDTO dto) {
        eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        AtualizacaoEvento dtoEvento = dto.evento();
        List<AtualizacaoParticipacaoNovoEvento> dtoNovasParticipacoes = dto.participacoes();

        Evento eventoSalvo = eventoService.salvarOuAtualizar(dtoEvento);

        if(dtoNovasParticipacoes != null) {
            for (AtualizacaoParticipacaoNovoEvento participacao : dtoNovasParticipacoes) {
                AtualizacaoParticipacao dtoParticipacao = new AtualizacaoParticipacao(
                        participacao.id(),
                        participacao.inscricao(),
                        participacao.tipo(),
                        participacao.pessoaId(),
                        eventoSalvo.getId()
                );

                participacaoService.salvarOuAtualizar(dtoParticipacao);
            }
        }

        return ResponseEntity.ok(Map.of("message", "Evento '" + eventoSalvo.getTema() + "' atualizado com sucesso."));
    }

    @DeleteMapping("/api/admin/eventos/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        List<Participacao> inscritos = participacaoService.procurarPorEvento(id);

        eventoService.cancelarEvento(id);

        emailProducer.publicarEventoCancelado(evento, inscritos);
        return ResponseEntity.ok(Map.of("message", "Evento cancelado com sucesso."));
    }

    @GetMapping("/api/admin/eventos/{id}/qrcode-inscricao")
    public ResponseEntity<byte[]> baixarQrCodeInscricao(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if (evento.getQrCodeInscricaoUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] imagem = qrCodeClient.baixarQrCode(evento.getQrCodeInscricaoUrl());
            String nomeArquivo = "qrcode-inscricao" + evento.getTema()
                    .replaceAll("\\s+", "-").toLowerCase() + ".png";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagem);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/admin/eventos/{id}/qrcode-confirmacao")
    public ResponseEntity<byte[]> baixarQrCodeConfirmacao(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if (evento.getQrCodeConfirmacaoUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] imagem = qrCodeClient.baixarQrCode(evento.getQrCodeConfirmacaoUrl());
            String nomeArquivo = "qrcode-confirmacao" + evento.getTema()
                    .replaceAll("\\s+", "-").toLowerCase() + ".png";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imagem);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/admin/eventos/{id}/participacoes")
    public ResponseEntity<Map<String, Object>> dadosConclusao(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

//        if (evento.getStatus() != StatusEventoEnum.EM_ANDAMENTO) {
//            throw new IllegalStateException("A lista de presença só pode ser consultada para eventos EM_ANDAMENTO.");
//        }

        return ResponseEntity.ok(Map.of(
                "participacoes", participacaoService.procurarPorEvento(id).stream()
                        .map(ParticipacaoComEventoResponse::from)
                        .toList()
        ));
    }

    @PostMapping("/api/admin/eventos/{id}/concluir")
    @Transactional
    public ResponseEntity<Map<String, String>> concluirEvento(
            @PathVariable Long id,
            @RequestBody(required = false) List<Long> presentes) {

        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if (evento.getStatus() != StatusEventoEnum.EM_ANDAMENTO) {
            throw new IllegalStateException("O evento só pode ser concluído quando estiver EM_ANDAMENTO.");
        }

        List<Participacao> participacoesDoEvento = participacaoService.procurarPorEvento(id);

        Set<Long> participacoesValidas = participacoesDoEvento.stream()
                .map(Participacao::getId)
                .collect(Collectors.toSet());

        if (presentes != null) {
            presentes.stream()
                    .filter(participacoesValidas::contains)
                    .forEach(participacaoService::marcarPresente);
        }

        Set<Long> todosPresentes = participacaoService.procurarPorEvento(id).stream()
                .filter(Participacao::isPresente)
                .map(Participacao::getId)
                .collect(Collectors.toSet());

        List<Certificado> certificadosEmail = certificadoService
                .gerarCertificadosParaParticipacoes(new ArrayList<>(todosPresentes));

        eventoService.concluirEvento(id);

        List<Participacao> inscritos = participacaoService.procurarPorEvento(id);
        emailProducer.publicarEventoConcluido(evento, inscritos);
        emailProducer.publicarCertificados(certificadosEmail, frontendUrl);

        return ResponseEntity.ok(Map.of("message", "Evento concluído e certificados gerados com sucesso."));
    }

    @PostMapping("/api/admin/eventos/{id}/confirmar-presenca")
    public ResponseEntity<String> confirmarPresenca(
            @PathVariable Long eventoId,
            @AuthenticationPrincipal Jwt jwt) {

        Long pessoaId = jwt.getClaim("userId");
        participacaoService.confirmarPresenca(eventoId, pessoaId);
        return ResponseEntity.ok("Presença confirmada com sucesso!");
    }

    private boolean inscricoesEncerradas(Evento evento) {
        if (evento.getData() == null || evento.getHorarioInicio() == null || evento.getHorarioInicio().isBlank()) {
            return false;
        }
        try {
            java.time.LocalDateTime inicioEvento = java.time.LocalDateTime.of(
                    evento.getData(),
                    java.time.LocalTime.parse(evento.getHorarioInicio())
            );
            return !inicioEvento.isAfter(java.time.LocalDateTime.now());
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
