package com.fatec.muttley;

import com.fatec.muttley.certificado.CertificadoRepository;
import com.fatec.muttley.certificado.CertificadoService;
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.medalha.MedalhaRepository;
import com.fatec.muttley.medalha.MedalhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private MedalhaService medalhaService;

    @GetMapping("/inicio")
    public ResponseEntity<Map<String, Object>> carregarEstatisticasAdmin() {
        Map<String, Object> response = new HashMap<>();

        response.put("proximosEventos", eventoService.procurarTodosOrdenarPorInicio());

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

        response.put("certificadosPorEvento", certificadosPorEvento.stream()
                .map(item -> new BarraEstatistica(item.getEventoTema(), item.getTotal(), percentual(item.getTotal(), maiorTotalCertificadosPorEvento)))
                .toList());

        response.put("medalhasPorParticipante", medalhasPorParticipante.stream()
                .map(item -> new BarraEstatistica(item.getParticipanteNome(), item.getTotal(), percentual(item.getTotal(), maiorTotalMedalhas)))
                .toList());

        response.put("certificadosUltimos30Dias", certificadosUltimos30Dias);
        response.put("variacaoCertificadosUltimos30Dias", calcularVariacaoPercentual(certificadosUltimos30Dias, certificadosPeriodoAnterior));
        response.put("eventosAtivos", eventoService.contarEventosAtivos());
        response.put("eventosAtivosNaSemana", eventoService.contarEventosAtivosNoPeriodo(hoje, hoje.plusDays(7)));

        return ResponseEntity.ok(response);
    }

    private int percentual(long valor, long maiorValor) {
        if (maiorValor <= 0) return 0;
        return Math.max(6, Math.round((valor * 100f) / maiorValor));
    }

    private long calcularVariacaoPercentual(long valorAtual, long valorAnterior) {
        if (valorAnterior == 0) return valorAtual > 0 ? 100 : 0;
        return Math.round(((valorAtual - valorAnterior) * 100f) / valorAnterior);
    }

    public record BarraEstatistica(String rotulo, long total, int percentual) {
    }
}