package com.fatec.muttley;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class InicioController {
    @GetMapping
    public String carregarIndex() {
        return "redirect:/admin/inicio";
    }
}
