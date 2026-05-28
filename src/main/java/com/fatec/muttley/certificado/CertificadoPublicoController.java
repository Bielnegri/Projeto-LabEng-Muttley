package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.pdf.PdfClient;
import com.fatec.muttley.pessoa.Pessoa;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
public class CertificadoPublicoController {

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private PdfClient pdfClient;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/api/certificados/{codigo}")
    public ResponseEntity<Map<String, Object>> dadosCertificadoPublico(
            @PathVariable String codigo,
            HttpServletRequest request) {
        Certificado certificado = buscarCertificado(codigo);
        return ResponseEntity.ok(Map.of(
                "certificado", certificado,
                "linkedinUrl", montarUrlLinkedIn(certificado, request)
        ));
    }

    @GetMapping("/api/certificados/{codigo}/preview")
    public ResponseEntity<Map<String, Object>> dadosPreview(@PathVariable String codigo,
                                                            HttpServletRequest request) {
        Certificado certificado = buscarCertificado(codigo);
        return ResponseEntity.ok(preencherModelo(certificado, request));
    }

    @GetMapping(value = "/api/certificados/{codigo}/preview-html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> previewHtml(@PathVariable String codigo,
                                              HttpServletRequest request) {
        Certificado certificado = buscarCertificado(codigo);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(renderizarModelo(certificado, request));
    }

    @GetMapping("/api/certificados/{codigo}/download")
    public ResponseEntity<byte[]> download(@PathVariable String codigo,
                                           HttpServletRequest request) throws IOException {
        Certificado certificado = buscarCertificado(codigo);
        String htmlProcessado = renderizarModelo(certificado, request);
        byte[] pdfBytes = pdfClient.gerarPdf(htmlProcessado);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"certificado.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    private Certificado buscarCertificado(String codigo) {
        return certificadoService.procurarPorCodigoValidacao(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificado nao encontrado."));
    }

    private String renderizarModelo(Certificado certificado, HttpServletRequest request) {
        Context context = new Context();
        context.setVariables(preencherModelo(certificado, request));
        return templateEngine.process("public/certificados/modeloPdf", context);
    }

    private Map<String, Object> preencherModelo(Certificado certificado, HttpServletRequest request) {
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        Pessoa pessoa = participacao != null ? participacao.getPessoa() : null;

        return Map.of(
                "pessoa", participacao != null && participacao.getTipo() != null ? participacao.getTipo() : "participante",
                "nome", pessoa != null ? pessoa.getNome() : "Participante",
                "preambulo", "Por participar do evento ",
                "evento", evento != null ? evento.getTema() : "Evento",
                "predicado", montarPredicado(evento),
                "duracao", calcularDuracao(evento),
                "data", formatarDataEmissao(certificado.getDataEmissao()),
                "baseUrl", montarBaseUrl(request)
        );
    }

    private String montarBaseUrl(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort());
        return baseUrl + "/";
    }

    private String montarUrlLinkedIn(Certificado certificado, HttpServletRequest request) {
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        LocalDate dataEmissao = certificado.getDataEmissao() != null
                ? certificado.getDataEmissao()
                : evento != null ? evento.getData() : null;
        String codigo = certificado.getCodigoValidacao();
        String urlCertificado = montarBaseUrl(request) + "certificados/" + codigo;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://www.linkedin.com/profile/add")
                .queryParam("startTask", "CERTIFICATION_NAME")
                .queryParam("name", montarNomeCertificado(evento))
                .queryParam("organizationName", "FATEC Zona Leste")
                .queryParam("certId", codigo)
                .queryParam("certUrl", urlCertificado);

        if (dataEmissao != null) {
            builder.queryParam("issueYear", dataEmissao.getYear());
            builder.queryParam("issueMonth", dataEmissao.getMonthValue());
        }

        return builder.build().encode().toUriString();
    }

    private String montarNomeCertificado(Evento evento) {
        if (evento == null || evento.getTema() == null || evento.getTema().isBlank()) {
            return "Certificado Muttley";
        }
        return "Certificado - " + evento.getTema();
    }

    private String montarPredicado(Evento evento) {
        if (evento == null || evento.getData() == null) {
            return "promovido pela FATEC Zona Leste.";
        }
        return "realizado no dia " + evento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + ", promovido pela FATEC Zona Leste.";
    }

    private String calcularDuracao(Evento evento) {
        if (evento == null || evento.getHorarioInicio() == null || evento.getHorarioFim() == null) {
            return "carga horaria nao informada.";
        }

        try {
            LocalTime inicio = LocalTime.parse(evento.getHorarioInicio());
            LocalTime fim = LocalTime.parse(evento.getHorarioFim());
            long minutos = Duration.between(inicio, fim).toMinutes();
            if (minutos <= 0) {
                return "carga horaria nao informada.";
            }

            long horas = minutos / 60;
            long minutosRestantes = minutos % 60;
            if (minutosRestantes == 0) {
                return horas + (horas == 1 ? " hora." : " horas.");
            }
            return horas + "h" + String.format("%02d", minutosRestantes) + ".";
        } catch (RuntimeException exception) {
            return "carga horaria nao informada.";
        }
    }

    private String formatarDataEmissao(LocalDate dataEmissao) {
        if (dataEmissao == null) {
            return "Sao Paulo";
        }
        return "Sao Paulo, " + dataEmissao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
