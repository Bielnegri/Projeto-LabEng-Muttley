package com.fatec.muttley.evento;

import com.fatec.muttley.certificado.CertificadoService;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;
import com.fatec.muttley.qrcode.QrCodeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private QrCodeService qrCodeService;

    @Autowired
    private CertificadoService certificadoService;

    @GetMapping("/api/eventos/{id}")
    public ResponseEntity<Map<String, Object>> buscarEventoPublico(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
        return ResponseEntity.ok(Map.of(
                "evento", evento,
                "inscricoesEncerradas", inscricoesEncerradas(evento)
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
    public ResponseEntity<Map<String, Object>> criar(@RequestBody @Valid AtualizacaoEvento dto,
                                                     HttpServletRequest request) {
        Evento eventoSalvo = eventoService.salvarOuAtualizar(dto);

        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() != 80 && request.getServerPort() != 443
                ? ":" + request.getServerPort() : "");

        String qrCodeUrl = qrCodeService.gerarUrlQrCode(baseUrl, eventoSalvo.getId(), eventoSalvo.getTema());
        eventoSalvo.setQrCodeUrl(qrCodeUrl);
        eventoService.salvarEntidade(eventoSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Evento '" + eventoSalvo.getTema() + "' criado com sucesso.",
                "id", eventoSalvo.getId()
        ));
    }

    @PutMapping("/api/admin/eventos/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoEvento dto) {
        eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));
        Evento eventoSalvo = eventoService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Evento '" + eventoSalvo.getTema() + "' atualizado com sucesso."));
    }

    @DeleteMapping("/api/admin/eventos/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> cancelar(@PathVariable Long id) {
        eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));
        eventoService.cancelarEvento(id);
        return ResponseEntity.ok(Map.of("message", "Evento cancelado com sucesso."));
    }

    @GetMapping("/api/admin/eventos/{id}/qrcode")
    public ResponseEntity<byte[]> baixarQrCode(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if (evento.getQrCodeUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] imagem = qrCodeService.baixarQrCode(evento.getQrCodeUrl());
            String nomeArquivo = "qrcode-" + evento.getTema()
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

        if (evento.getStatus() != StatusEventoEnum.EM_ANDAMENTO) {
            throw new IllegalStateException("A lista de presença só pode ser consultada para eventos EM_ANDAMENTO.");
        }

        return ResponseEntity.ok(Map.of(
                "evento", evento,
                "participacoes", participacaoService.procurarPorEvento(id)
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

        Set<Long> participacoesDoEvento = participacaoService.procurarPorEvento(id).stream()
                .map(Participacao::getId)
                .collect(Collectors.toSet());

        List<Long> presentesValidos = (presentes == null ? List.<Long>of() : presentes).stream()
                .filter(participacoesDoEvento::contains)
                .toList();

        certificadoService.gerarCertificadosParaParticipacoes(presentesValidos);
        eventoService.concluirEvento(id);

        return ResponseEntity.ok(Map.of("message", "Evento concluído e certificados gerados com sucesso."));
    }

    private boolean inscricoesEncerradas(Evento evento) {
        if (evento.getData() == null || evento.getHorarioInicio() == null || evento.getHorarioInicio().isBlank()) {
            return false;
        }
        try {
            java.time.LocalDateTime inicioEvento = java.time.LocalDateTime.of(
                    evento.getData().toLocalDate(),
                    java.time.LocalTime.parse(evento.getHorarioInicio())
            );
            return !inicioEvento.isAfter(java.time.LocalDateTime.now());
        } catch (RuntimeException exception) {
            return false;
        }
    }
}