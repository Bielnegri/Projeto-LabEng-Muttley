package com.fatec.muttley;

import com.fatec.muttley.pessoa.AtualizacaoPessoa;
import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/login")
    public String carregarLogin() {
        return "public/auth/login";
    }

    @GetMapping("/register")
    public String carregarCadastro() {
        return "public/auth/register";
    }

    @PostMapping("/public/auth/register/salvar")
    public String cadastrarUsuario(@ModelAttribute("pessoa") @Valid AtualizacaoPessoa dto,
                                   RedirectAttributes redirectAttributes){
        try {
            if(pessoaService.procurarPorCpf(dto.cpf()).isPresent()){
                String mensagem = "Usuário já cadastrado com esse CPF.";
                redirectAttributes.addFlashAttribute("message", mensagem);
                return "redirect:/register";
            }
            Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
            String mensagem ="Usuário '" + pessoaSalva.getNome() + "' cadastrado com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/login";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/public/auth/login/entrar")
    public String validarCredenciais(@ModelAttribute("cpf") String cpf, @ModelAttribute("senha") String senha,
                                     RedirectAttributes redirectAttributes){
        try {
            Optional<Pessoa> pessoaOptional = pessoaService.procurarPorCpf(cpf);

            if (pessoaOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "CPF ou senha incorretos");
                return "redirect:/login";
            }

            Pessoa pessoaSalva = pessoaOptional.get();

            if (!Objects.equals(senha, pessoaSalva.getSenha())) {
                redirectAttributes.addFlashAttribute("message", "CPF ou senha incorretos");
                return "redirect:/login";
            }

            //TODO URL correta pós login
            redirectAttributes.addFlashAttribute("message", "Login efetuado com sucesso");
            return "redirect:/login";

        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/login";
        }
    }
}
