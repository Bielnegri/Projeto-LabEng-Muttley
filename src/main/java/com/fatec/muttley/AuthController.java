package com.fatec.muttley;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String carregarLogin() {
        return "public/auth/login";
    }

    @GetMapping("/register")
    public String carregarCadastro() {
        return "public/auth/register";
    }
}
