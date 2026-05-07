package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.pessoa.Pessoa;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class CertificadoPublicoController {
    @Autowired
    private CertificadoService certificadoService;

    @GetMapping("/certificados/{codigo}")
    public String exibirPaginaPublica(@PathVariable("codigo") String codigo, Model model, HttpServletRequest request) {
        Certificado certificado = buscarCertificado(codigo);
        model.addAttribute("certificado", certificado);
        model.addAttribute("linkedinUrl", montarUrlLinkedIn(certificado, request));
        return "certificado/publico";
    }

    @GetMapping("/certificados/{codigo}/preview")
    public String exibirPreview(@PathVariable("codigo") String codigo, Model model) {
        Certificado certificado = buscarCertificado(codigo);
        preencherModelo(certificado, model);
        model.addAttribute("preview", true);
        return "certificado/modelo";
    }

    @GetMapping("/certificados/{codigo}/download")
    public String exibirDownload(@PathVariable("codigo") String codigo, Model model) {
        Certificado certificado = buscarCertificado(codigo);
        preencherModelo(certificado, model);
        return "certificado/modeloPdf";
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

    private String montarNomeCertificado(Evento evento) {
        if (evento == null || evento.getTema() == null || evento.getTema().isBlank()) {
            return "Certificado Muttley";
        }
        return "Certificado - " + evento.getTema();
    }

    private void preencherModelo(Certificado certificado, Model model) {
        Participacao participacao = certificado.getParticipacao();
        Evento evento = participacao != null ? participacao.getEvento() : null;
        Pessoa pessoa = participacao != null ? participacao.getPessoa() : null;

        model.addAttribute("pessoa", participacao != null && participacao.getTipo() != null ? participacao.getTipo() : "participante");
        model.addAttribute("nome", pessoa != null ? pessoa.getNome() : "Participante");
        model.addAttribute("preambulo", "Por participar do evento ");
        model.addAttribute("evento", evento != null ? evento.getTema() : "Evento");
        model.addAttribute("predicado", montarPredicado(evento));
        model.addAttribute("duracao", calcularDuracao(evento));
        model.addAttribute("data", formatarDataEmissao(certificado.getDataEmissao()));
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
