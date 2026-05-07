package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/certificados")
public class CertificadoController {
    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private EventoService eventoService;

    @GetMapping
    public String carregaPaginaCertificados(Model model) {
        model.addAttribute("listaCertificados", certificadoService.procurarTodos());
        model.addAttribute("eventosAguardandoCertificado", eventoService.procurarEventosEncerradosAguardandoCertificado());
        model.addAttribute("ultimosCertificados", certificadoService.procurarUltimosEmitidos());
        return "certificado/listagem";
    }

    @GetMapping("/modelo")
    public String mostraModelo(@RequestParam(required = false, defaultValue = "false") boolean preview, Model model) {
        model.addAttribute("preview", preview);
        model.addAttribute("pessoa", "participante");
        model.addAttribute("nome", "Nome do participante");
        model.addAttribute("preambulo", "Por participar do evento ");
        model.addAttribute("evento", "Modelo de Evento Academico");
        model.addAttribute("predicado", "realizado pela FATEC Zona Leste.");
        model.addAttribute("duracao", "2 (duas) horas.");
        model.addAttribute("data", "Sao Paulo, 30 de marco de 2026");
        return "certificado/modelo";
    }
}
