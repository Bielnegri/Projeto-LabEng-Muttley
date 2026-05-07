package com.fatec.muttley;

import com.fatec.muttley.evento.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
