package com.fatec.muttley.aluno;

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

    @GetMapping("/formulario")
    public String mostrarFormulario (@RequestParam(required = false) Long id, Model model) {
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

    @GetMapping ("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoAluno dto;
        try {
            if(id != null) {
                Aluno aluno = alunoService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Aluno não encontrado"));
                //mapear aluno para AtualizacaoAluno
                dto = alunoMapper.toAtualizacaoDto(aluno);
                model.addAttribute("aluno", dto);
            }
            return "aluno/formulario";
        } catch (EntityNotFoundException e) {
            //resolver erros
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/aluno/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("aluno") @Valid AtualizacaoAluno dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            // Recarrega dados necessários para mostrar erros
            return "aluno/formulario";
        }
        try {
            Aluno alunoSalvo = alunoService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Aluno '" + alunoSalvo.getNome() + "' atualizado com sucesso!"
                    : "Aluno '" + alunoSalvo.getNome() + "' criado com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/aluno/listagem";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/aluno/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarAluno(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            alunoService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O aluno " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/aluno/listagem";
    }
}
