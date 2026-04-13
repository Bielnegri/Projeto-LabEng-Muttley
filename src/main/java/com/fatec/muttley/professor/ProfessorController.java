package com.fatec.muttley.professor;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
