package com.fatec.muttley.pessoa;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaMapper pessoaMapper;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario (Model model){
        //devolver DTO
        model.addAttribute("listaPessoas", pessoaService.procurarTodos());
        return "pessoa/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario (@RequestParam(required = false) Long id, Model model) {
        AtualizacaoPessoa dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Pessoa pessoa = pessoaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
            dto = pessoaMapper.toAtualizacaoDto(pessoa);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoPessoa(null, "", "", "", "");
        }
        model.addAttribute("pessoa", dto);
        return "pessoa/formulario";
    }

    @GetMapping ("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoPessoa dto;
        try {
            if(id != null) {
                Pessoa pessoa = pessoaService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Pessoa não encontrada"));
                //mapear pessoa para AtualizacaoPessoa
                dto = pessoaMapper.toAtualizacaoDto(pessoa);
                model.addAttribute("pessoa", dto);
            }
            return "pessoa/formulario";
        } catch (EntityNotFoundException e) {
            //resolver erros
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pessoa/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("pessoa") @Valid AtualizacaoPessoa dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            // Recarrega dados necessários para mostrar erros
            return "pessoa/formulario";
        }
        try {
            Pessoa pessoaSalvo = pessoaService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Pessoa '" + pessoaSalvo.getNome() + "' atualizada com sucesso!"
                    : "Pessoa '" + pessoaSalvo.getNome() + "' criada com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/pessoa/listagem";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pessoa/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarPessoa(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            pessoaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "A pessoa " + id + " foi apagada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/pessoa/listagem";
    }
}
