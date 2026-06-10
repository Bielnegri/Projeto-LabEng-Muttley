package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.pdf.PdfClient;
import com.fatec.muttley.pessoa.Pessoa;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

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
    public ResponseEntity<byte[]> preview(@PathVariable String codigo, Model model) throws IOException {
        Certificado certificado = buscarCertificado(codigo);
        preencherModelo(certificado, model);
        return gerarPdf(model, "inline");
    }

    @GetMapping("/api/certificados/{codigo}/download")
    public ResponseEntity<byte[]> download(@PathVariable String codigo, Model model) throws IOException {
        Certificado certificado = buscarCertificado(codigo);
        preencherModelo(certificado, model);
        return gerarPdf(model, "attachment");
    }

    private Certificado buscarCertificado(String codigo) {
        return certificadoService.procurarPorCodigoValidacao(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificado não encontrado."));
    }

    private String montarUrlLinkedIn(Certificado certificado, HttpServletRequest request) {
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        LocalDate dataEmissao = certificado.getDataEmissao() != null ? certificado.getDataEmissao() : evento != null ? evento.getData() : null;
        String codigo = certificado.getCodigoValidacao();
        String urlCertificado = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/certificados/" + codigo)
                .replaceQuery(null)
                .build()
                .toUriString();

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

    private ResponseEntity<byte[]> gerarPdf(Model model, String disposition) throws IOException {
        Context context = new Context();
        context.setVariables(model.asMap());
        String htmlProcessado = templateEngine.process("public/certificados/modeloPdf", context);

        byte[] pdfBytes = pdfClient.gerarPdf(htmlProcessado);

        System.out.println("PDF gerado com sucesso!");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"certificado.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    private String montarNomeCertificado(Evento evento) {
        if (evento == null || evento.getTema() == null || evento.getTema().isBlank()) {
            return "Certificado Muttley";
        }
        return "Certificado - " + evento.getTema();
    }

    private Map<String, Object> preencherModelo(Certificado certificado) {
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        Pessoa pessoa = participacao != null ? participacao.getPessoa() : null;

        String assinaturaBase64 = "";
        if (certificado.getCaminhoAssinaturaVisual() != null && !certificado.getCaminhoAssinaturaVisual().isBlank()) {
            try {
                Path path = Paths.get(certificado.getCaminhoAssinaturaVisual());
                byte[] imageBytes = Files.readAllBytes(path);
                assinaturaBase64 = Base64.getEncoder().encodeToString(imageBytes);
            } catch (Exception e) {
                System.out.println("Erro ao converter assinatura: " + e.getMessage());
            }
        }

        return Map.of(
                "certificado", certificado,
                "assinaturaBase64", assinaturaBase64, // <-- ENVIADO EM TEXTO AQUI
                "pessoa", participacao != null && participacao.getTipo() != null ? participacao.getTipo() : "participante",
                "nome", pessoa != null ? pessoa.getNome() : "Participante",
                "preambulo", "Por participar do evento ",
                "evento", evento != null ? evento.getTema() : "Evento",
                "predicado", montarPredicado(evento),
                "duracao", calcularDuracao(evento),
                "data", formatarDataEmissao(certificado.getDataEmissao())
        );
    }

    private void preencherModelo(Certificado certificado, Model model) {
        preencherModelo(certificado).forEach(model::addAttribute);
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
            return "carga horária não informada.";
        }

        try {
            LocalTime inicio = LocalTime.parse(evento.getHorarioInicio());
            LocalTime fim = LocalTime.parse(evento.getHorarioFim());
            long minutos = Duration.between(inicio, fim).toMinutes();
            if (minutos <= 0) {
                return "carga horária não informada.";
            }

            long horas = minutos / 60;
            long minutosRestantes = minutos % 60;
            if (minutosRestantes == 0) {
                return horas + (horas == 1 ? " hora." : " horas.");
            }
            return horas + "h" + String.format("%02d", minutosRestantes) + ".";
        } catch (RuntimeException exception) {
            return "carga horária não informada.";
        }
    }

    private String formatarDataEmissao(LocalDate dataEmissao) {
        if (dataEmissao == null) {
            return "São Paulo";
        }
        return "São Paulo, " + dataEmissao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @GetMapping("/{id}/assinatura-visual")
    public ResponseEntity<Resource> exibirImagemPublica(@PathVariable Long id) {
        try {
            Certificado certificado = certificadoService.procurarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

            String caminhoString = certificado.getCaminhoAssinaturaVisual();
            if (caminhoString == null || caminhoString.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path caminhoArquivo = Paths.get(caminhoString);
            Resource recurso = new UrlResource(caminhoArquivo.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
