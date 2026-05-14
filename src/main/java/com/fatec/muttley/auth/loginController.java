package com.fatec.muttley.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class loginController {

    @PostMapping("/public/auth/login/entrar")
    public String validarCredenciais(){
        System.out.println("validando...");
        return "/public/auth/login";
    }
}
