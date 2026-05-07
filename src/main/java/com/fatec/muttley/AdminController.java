package com.fatec.muttley;

import com.fatec.muttley.evento.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fatec.muttley.email.GmailService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/inicio")
    public String carregarPaginaInicialAdmin(Model model) {
        model.addAttribute("proximosEventos", eventoService.procurarTodosOrdenarPorInicio());
        return "admin/inicio";
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