package com.fatec.muttley.aluno;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @Autowired
    private AlunoMapper alunoMapper;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario (Model model){
        //devolver DTO
        model.addAttribute("listaAlunos", alunoService.procurarTodos());
        return "aluno/listagem";
    }

    //Novo GetMapping com DTO e Mapper
    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoAluno dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Aluno aluno = alunoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
            dto = alunoMapper.toAtualizacaoDto(aluno);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoAluno(null, "", "", "", "", "");
        }
        model.addAttribute("aluno", dto);
        return "aluno/formulario";
    }
}
