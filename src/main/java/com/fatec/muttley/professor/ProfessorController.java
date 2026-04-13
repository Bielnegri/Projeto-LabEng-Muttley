package com.fatec.muttley.professor;

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
@RequestMapping("/professor")
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProfessorMapper professorMapper;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario (Model model){
        //devolverDTO
        model.addAttribute("listaProfessores", professorService.procurarTodos());
        return "professor/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario (@RequestParam(required = false) Long id, Model model) {
        AtualizacaoProfessor dto;
        if (id != null){
            //edição: Carrega dados existentes
            Professor professor = professorService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
            dto = professorMapper.toAtualizacaoDto(professor);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoProfessor(null, "", "", "");
        }
        model.addAttribute("professor", dto);
        return "professor/formulario";
    }

    @GetMapping ("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoProfessor dto;
        try {
            if(id != null) {
                Professor professor = professorService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Professor não encontrado"));
                //mapear professor para AtualizacaoProfessor
                dto = professorMapper.toAtualizacaoDto(professor);
                model.addAttribute("professor", dto);
            }
            return "professor/formulario";
        } catch (EntityNotFoundException e) {
            //resolver erros
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/professor/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("professor") @Valid AtualizacaoProfessor dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            // Recarrega dados necessários para mostrar erros
            return "professor/formulario";
        }
        try {
            Professor professorSalvo = professorService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Professor '" + professorSalvo.getNome() + "' atualizado com sucesso!"
                    : "Professor '" + professorSalvo.getNome() + "' criado com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/professor/listagem";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/professor/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarProfessor(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            professorService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O professor " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/professor/listagem";
    }
}
