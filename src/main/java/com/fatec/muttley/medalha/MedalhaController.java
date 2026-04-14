package com.fatec.muttley.medalha;

import com.fatec.muttley.aluno.AlunoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                    .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrado"));
            dto = medalhaMapper.toAtualizacaoDto(medalha);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoMedalha(null, "", "", null);
        }
        model.addAttribute("medalha", dto);
        model.addAttribute("alunos", alunoService.procurarTodos());
        return "medalha/formulario";
    }
}
