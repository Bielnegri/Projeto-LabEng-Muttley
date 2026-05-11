package com.fatec.muttley;

import com.fatec.muttley.certificado.CertificadoRepository;
import com.fatec.muttley.certificado.CertificadoService;
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.medalha.MedalhaRepository;
import com.fatec.muttley.medalha.MedalhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fatec.muttley.email.GmailService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private MedalhaService medalhaService;

    @GetMapping("/inicio")
    public String carregarPaginaInicialAdmin(Model model) {
        model.addAttribute("proximosEventos", eventoService.procurarTodosOrdenarPorInicio());
        popularEstatisticas(model);
        return "admin/inicio";
    }

    private void popularEstatisticas(Model model) {
        List<CertificadoRepository.CertificadosPorEvento> certificadosPorEvento =
                certificadoService.procurarTotaisPorEvento(8);
        long maiorTotalCertificadosPorEvento = certificadosPorEvento.stream()
                .mapToLong(CertificadoRepository.CertificadosPorEvento::getTotal)
                .max()
                .orElse(0);

        List<MedalhaRepository.MedalhasPorParticipante> medalhasPorParticipante =
                medalhaService.procurarTotaisPorParticipante(7);
        long maiorTotalMedalhas = medalhasPorParticipante.stream()
                .mapToLong(MedalhaRepository.MedalhasPorParticipante::getTotal)
                .max()
                .orElse(0);

        LocalDate hoje = LocalDate.now();
        LocalDate inicioUltimos30Dias = hoje.minusDays(30);
        LocalDate inicioPeriodoAnterior = inicioUltimos30Dias.minusDays(30);
        long certificadosUltimos30Dias = certificadoService.contarEmitidosDesde(inicioUltimos30Dias);
        long certificadosPeriodoAnterior = certificadoService.contarEmitidosEntre(inicioPeriodoAnterior, inicioUltimos30Dias);

        model.addAttribute("certificadosPorEvento", certificadosPorEvento.stream()
                .map(item -> new BarraEstatistica(
                        item.getEventoTema(),
                        item.getTotal(),
                        percentual(item.getTotal(), maiorTotalCertificadosPorEvento)
                ))
                .toList());
        model.addAttribute("medalhasPorParticipante", medalhasPorParticipante.stream()
                .map(item -> new BarraEstatistica(
                        item.getParticipanteNome(),
                        item.getTotal(),
                        percentual(item.getTotal(), maiorTotalMedalhas)
                ))
                .toList());
        model.addAttribute("certificadosUltimos30Dias", certificadosUltimos30Dias);
        model.addAttribute("variacaoCertificadosUltimos30Dias",
                calcularVariacaoPercentual(certificadosUltimos30Dias, certificadosPeriodoAnterior));
        model.addAttribute("eventosAtivos", eventoService.contarEventosAtivos());
        model.addAttribute("eventosAtivosNaSemana", eventoService.contarEventosAtivosNoPeriodo(hoje, hoje.plusDays(7)));
    }

    private int percentual(long valor, long maiorValor) {
        if (maiorValor <= 0) {
            return 0;
        }
        return Math.max(6, Math.round((valor * 100f) / maiorValor));
    }

    private long calcularVariacaoPercentual(long valorAtual, long valorAnterior) {
        if (valorAnterior == 0) {
            return valorAtual > 0 ? 100 : 0;
        }
        return Math.round(((valorAtual - valorAnterior) * 100f) / valorAnterior);
    }

    public record BarraEstatistica(String rotulo, long total, int percentual) {
    }
}

//    @PostMapping("/testeEmail")
//    public String testeEmail(RedirectAttributes redirectAttributes) {
//        try {
//            GmailService.enviarEmail(
//                    "gabrielbnegri@gmail.com",
//                    "Envio automatico do Muttley",
//                    "ATENÇÃO, ESTE EMAIL FOI ENVIADO AUTOMATICAMENTE"
//            );
//            redirectAttributes.addFlashAttribute("sucesso", "E-mail enviado!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
//        }
//        return "redirect:/admin/inicio";
//    }
//}