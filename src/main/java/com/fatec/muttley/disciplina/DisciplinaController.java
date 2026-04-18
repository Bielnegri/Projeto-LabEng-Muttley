package com.fatec.muttley.disciplina;

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
@RequestMapping("/disciplina")
public class DisciplinaController {
    @Autowired
        private DisciplinaService disciplinaService;
    @Autowired
        private DisciplinaMapper disciplinaMapper;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario(Model model){
        model.addAttribute("listaDisciplinas", disciplinaService.procurarTodas());
        return "disciplina/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario (@RequestParam(required = false)Long id, Model model){
        AtualizacaoDisciplina dto;
        if(id != null){
            Disciplina disciplina = disciplinaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
            dto = disciplinaMapper.toAtualizacaoDto(disciplina);
        } else {
            dto = new AtualizacaoDisciplina(null, "", "", "");
        }
        model.addAttribute("disciplina", dto);
        return "disciplina/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id")Long id, Model model, RedirectAttributes redirectAttributes) {
        AtualizacaoDisciplina dto;
        try{
            if (id != null){
                Disciplina disciplina = disciplinaService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Disciplina não encontrada"));
                dto = disciplinaMapper.toAtualizacaoDto(disciplina);
                model.addAttribute("disciplina", dto);
            }
            return "disciplina/formulario";
        } catch (EntityNotFoundException exception){
            redirectAttributes.addFlashAttribute("Erro", exception.getMessage());
            return "redirect:/disciplina/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("disciplina")@Valid AtualizacaoDisciplina dto, BindingResult result, RedirectAttributes redirectAttributes, Model model){
        if (result.hasErrors()){
            return "disciplina/formulario";
        }
        try{
            Disciplina disciplinaSalva = disciplinaService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                ? "Disciplina '"+disciplinaSalva.getNome() +"' atualizada com êxito"
                : "Disicplina '"+disciplinaSalva.getNome()+"' criada com êxito";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/disciplina/listagem";
        }catch (EntityNotFoundException exception){
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/disciplina/formulario" + (dto.id() != null ? "?id= " + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarDisciplina(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        try{
            disciplinaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O aluno " + id + " foi deletado.");
        }catch (Exception exception){
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return  "redirect:/disciplina/listagem";
    }
}
