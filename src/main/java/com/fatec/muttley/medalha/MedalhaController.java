package com.fatec.muttley.medalha;

import com.fatec.muttley.aluno.AlunoService;
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
@RequestMapping("/medalha")
public class MedalhaController {
    @Autowired
    private MedalhaService medalhaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private MedalhaMapper medalhaMapper;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario (Model model){
        //devolver DTO
        model.addAttribute("listaMedalhas", medalhaService.procurarTodos());
        return "medalha/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoMedalha dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Medalha medalha = medalhaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada"));
            dto = medalhaMapper.toAtualizacaoDto(medalha);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoMedalha(null, "", "", null);
        }
        model.addAttribute("medalha", dto);
        model.addAttribute("alunos", alunoService.procurarTodos());
        return "medalha/formulario";
    }
    @GetMapping ("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoMedalha dto;
        try {
            if(id != null) {
                Medalha medalha = medalhaService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Medalha não encontrada"));
                //mapear medalha para AtualizacaoMedalha
                dto = medalhaMapper.toAtualizacaoDto(medalha);
                model.addAttribute("medalha", dto);
            }
            return "medalha/formulario";
        } catch (EntityNotFoundException e) {
            //resolver erros
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/medalha/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("medalha") @Valid AtualizacaoMedalha dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            // Recarrega dados necessários para mostrar erros
            return "medalha/formulario";
        }
        try {
            Medalha medalhaSalvo = medalhaService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Medalha '" + medalhaSalvo.getNome() + "' atualizada com sucesso!"
                    : "Medalha '" + medalhaSalvo.getNome() + "' criada com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/medalha/listagem";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/medalha/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarMedalha(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            medalhaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "A medalha " + id + " foi apagada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/medalha/listagem";
    }
}
