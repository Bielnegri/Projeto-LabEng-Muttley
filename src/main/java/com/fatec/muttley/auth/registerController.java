package com.fatec.muttley.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class registerController {

    @PostMapping("/public/auth/register/salvar")
    public String cadastrarUsuario(){
        System.out.println("cadastrando...");
        return "public/auth/register";
    }

}
