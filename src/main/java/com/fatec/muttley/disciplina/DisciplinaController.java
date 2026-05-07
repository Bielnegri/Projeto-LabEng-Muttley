package com.fatec.muttley.disciplina;

import com.fatec.muttley.professor.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/disciplinas")
public class DisciplinaController {
    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    @GetMapping
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("listaDisciplinas", disciplinaService.procurarTodas());
        return "admin/disciplinas/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoDisciplina dto;
        if (id != null) {
            Disciplina disciplina = disciplinaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina nao encontrada."));
            dto = disciplinaMapper.toAtualizacaoDto(disciplina);
        } else {
            dto = new AtualizacaoDisciplina(null, "", "", "", null);
        }
        model.addAttribute("disciplina", dto);
        model.addAttribute("professores", professorService.procurarTodos());
        return "admin/disciplinas/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaFormularioPorId(@PathVariable("id") Long id,
                                         Model model,
                                         RedirectAttributes redirectAttributes) {
        try {
            Disciplina disciplina = disciplinaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina nao encontrada."));
            model.addAttribute("disciplina", disciplinaMapper.toAtualizacaoDto(disciplina));
            model.addAttribute("professores", professorService.procurarTodos());
            return "admin/disciplinas/formulario";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/admin/disciplinas/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("disciplina") @Valid AtualizacaoDisciplina dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("professores", professorService.procurarTodos());
            return "admin/disciplinas/formulario";
        }

        try {
            Disciplina disciplinaSalva = disciplinaService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Disciplina '" + disciplinaSalva.getNome() + "' atualizada com sucesso."
                    : "Disciplina '" + disciplinaSalva.getNome() + "' criada com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/admin/disciplinas";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/admin/disciplinas/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarDisciplina(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            disciplinaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "A disciplina " + id + " foi deletada.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/admin/disciplinas";
    }
}
