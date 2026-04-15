package com.fatec.muttley.disciplina;

import com.fatec.muttley.certificado.CertificadoService;
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
    @Autowired
    private CertificadoService certificadoService;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario(Model model){
        model.addAttribute("listaDisciplinas", certificadoService.procurarTodos());
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
            return "aluno/formulario";
        } catch (EntityNotFoundException exception){
            redirectAttributes.addFlashAttribute("Erro", exception.getMessage());
            return "redirect:/disciplina/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("disciplina")@Valid AtualizacaoDisciplina dto, BindingResult result, RedirectAttributes redirectAttributes, Model model){
        if (){

        }
        try{

        }catch (){

        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarDisciplina(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        try{

        }catch (){

        }
    }
}
