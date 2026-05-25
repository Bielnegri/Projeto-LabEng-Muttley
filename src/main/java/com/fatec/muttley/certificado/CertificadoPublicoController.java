package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.pdf.PdfService;
import com.fatec.muttley.pessoa.Pessoa;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
<<<<<<< Updated upstream
import java.util.Map;
=======
>>>>>>> Stashed changes

@Controller
public class CertificadoPublicoController {
    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private TemplateEngine templateEngine;

<<<<<<< Updated upstream
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
    public ResponseEntity<Map<String, Object>> dadosPreview(@PathVariable String codigo) {
        Certificado certificado = buscarCertificado(codigo);
        return ResponseEntity.ok(preencherModelo(certificado));
    }

    @GetMapping("/api/certificados/{codigo}/download")
    public ResponseEntity<byte[]> download(
            @PathVariable String codigo,
            Model model,
            HttpServletResponse response) throws IOException {
=======
    @GetMapping("/certificados/{codigo}")
    public String exibirPaginaPublica(@PathVariable("codigo") String codigo, Model model, HttpServletRequest request) {
        Certificado certificado = buscarCertificado(codigo);
        model.addAttribute("certificado", certificado);
        model.addAttribute("linkedinUrl", montarUrlLinkedIn(certificado, request));
        return "public/certificados/publico";
    }

    @GetMapping("/certificados/{codigo}/preview")
    public String exibirPreview(@PathVariable("codigo") String codigo, Model model) {
        Certificado certificado = buscarCertificado(codigo);
        preencherModelo(certificado, model);
        model.addAttribute("preview", true);
        return "public/certificados/modelo";
    }

    @GetMapping("/certificados/{codigo}/download")
    public ResponseEntity<byte[]> exibirDownload(@PathVariable("codigo") String codigo, Model model, HttpServletResponse response) throws IOException{
>>>>>>> Stashed changes
        Certificado certificado = buscarCertificado(codigo);
        preencherModelo(certificado, model);
        return baixar(model);
    }

    private Certificado buscarCertificado(String codigo) {
        return certificadoService.procurarPorCodigoValidacao(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificado não encontrado."));
    }

    private String montarUrlLinkedIn(Certificado certificado, HttpServletRequest request) {
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        Date dataEmissao = certificado.getDataEmissao() != null ? certificado.getDataEmissao() : evento != null ? evento.getData() : null;
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
            builder.queryParam("issueYear", dataEmissao.toLocalDate().getYear());
            builder.queryParam("issueMonth", dataEmissao.toLocalDate().getMonthValue());
        }

        return builder.build().encode().toUriString();
    }

<<<<<<< Updated upstream
    private ResponseEntity<byte[]> baixar(Model model) throws IOException {
=======
    public ResponseEntity<byte[]> baixar(Model model) throws IOException {
>>>>>>> Stashed changes
        Context context = new Context();
        context.setVariables(model.asMap());
        String htmlProcessado = templateEngine.process("public/certificados/modeloPdf", context);

        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlProcessado);

        System.out.println("PDF gerado com sucesso!");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"certificado.pdf\"")
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

<<<<<<< Updated upstream
    private Map<String, Object> preencherModelo(Certificado certificado) {
=======
    private void preencherModelo(Certificado certificado, Model model) {
>>>>>>> Stashed changes
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        Pessoa pessoa = participacao != null ? participacao.getPessoa() : null;

<<<<<<< Updated upstream
        return Map.of(
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
=======
        model.addAttribute("pessoa", participacao != null && participacao.getTipo() != null ? participacao.getTipo() : "participante");
        model.addAttribute("nome", pessoa != null ? pessoa.getNome() : "Participante");
        model.addAttribute("preambulo", "Por participar do evento ");
        model.addAttribute("evento", evento != null ? evento.getTema() : "Evento");
        model.addAttribute("predicado", montarPredicado(evento));
        model.addAttribute("duracao", calcularDuracao(evento));
        model.addAttribute("data", formatarDataEmissao(certificado.getDataEmissao()));
>>>>>>> Stashed changes
    }

    private String montarPredicado(Evento evento) {
        if (evento == null || evento.getData() == null) {
            return "promovido pela FATEC Zona Leste.";
        }
        return "realizado no dia " + evento.getData().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
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

    private String formatarDataEmissao(Date dataEmissao) {
        if (dataEmissao == null) {
            return "São Paulo";
        }
        return "São Paulo, " + dataEmissao.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
