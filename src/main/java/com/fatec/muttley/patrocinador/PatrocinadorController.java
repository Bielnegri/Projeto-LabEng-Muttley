package com.fatec.muttley.patrocinador;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patrocinador")
public class PatrocinadorController {

    @Autowired
    private PatrocinadorService patrocinadorService;

    @Autowired
    private PatrocinadorMapper patrocinadorMapper;

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("listaPatrocinadors", patrocinadorService.procurarTodos());
        return "patrocinador/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoPatrocinador dto;
        if (id != null) {
            Patrocinador patrocinador = patrocinadorService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado."));
            dto = patrocinadorMapper.toAtualizacaoDto(patrocinador);
        } else {
            dto = new AtualizacaoPatrocinador(null, "", "", 0, "", "", "");
        }
        model.addAttribute("patrocinador", dto);
        return "patrocinador/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaFormularioPorId(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Patrocinador patrocinador = patrocinadorService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Patrocinador não encontrado."));
            AtualizacaoPatrocinador dto = patrocinadorMapper.toAtualizacaoDto(patrocinador);
            model.addAttribute("patrocinador", dto);
            return "patrocinador/formulario";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/patrocinador/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("patrocinador") @Valid AtualizacaoPatrocinador dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            return "patrocinador/formulario";
        }
        try {
            Patrocinador patrocinadorSalvo = patrocinadorService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Patrocinador '" + patrocinadorSalvo.getNome() + "' atualizado com sucesso."
                    : "Patrocinador '" + patrocinadorSalvo.getNome() + "' criado com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/patrocinador/listagem";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/patrocinador/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarPatrocinador(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            patrocinadorService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "Patrocinador: " + id + " cancelado com sucesso.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/patrocinador/listagem";
    }
}
