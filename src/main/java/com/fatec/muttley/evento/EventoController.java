package com.fatec.muttley.evento;

import com.fatec.muttley.certificado.Certificado;
import com.fatec.muttley.certificado.CertificadoService;
import com.fatec.muttley.email.EmailProducer;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.participacao.AtualizacaoParticipacao;
import com.fatec.muttley.participacao.AtualizacaoParticipacaoNovoEvento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;
import com.fatec.muttley.qrcode.QrCodeClient;
import com.fatec.muttley.qrcode.QrCodeProducer;
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
    private QrCodeProducer qrCodeProducer;

    @Autowired
    private QrCodeClient qrCodeClient;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private EmailProducer emailProducer;

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

    @PostMapping("/api/admin/eventos/criar")
    public ResponseEntity<Map<String, Object>> criar(@RequestBody @Valid EventoComParticipacaoDTO dto,
                                                     HttpServletRequest request) {
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

        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() != 80 && request.getServerPort() != 443
                ? ":" + request.getServerPort() : "");

        qrCodeProducer.publicarGeracaoQrCode(eventoSalvo, baseUrl);

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
        dto = dto.withId(id);
        Evento eventoSalvo = eventoService.salvarOuAtualizar(dto);
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

    @GetMapping("/api/admin/eventos/{id}/qrcode")
    public ResponseEntity<byte[]> baixarQrCode(@PathVariable Long id) {
        Evento evento = eventoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        if (evento.getQrCodeUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] imagem = qrCodeClient.baixarQrCode(evento.getQrCodeUrl());
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

//        if (evento.getStatus() != StatusEventoEnum.EM_ANDAMENTO) {
//            throw new IllegalStateException("A lista de presença só pode ser consultada para eventos EM_ANDAMENTO.");
//        }

        return ResponseEntity.ok(Map.of(
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

        List<Certificado> certificadosEmail = certificadoService.gerarCertificadosParaParticipacoes(presentesValidos);
        eventoService.concluirEvento(id);

        List<Participacao> inscritos = participacaoService.procurarPorEvento(id);
        emailProducer.publicarEventoConcluido(evento, inscritos);
        emailProducer.publicarCertificados(certificadosEmail);

        return ResponseEntity.ok(Map.of("message", "Evento concluído e certificados gerados com sucesso."));
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