package com.fatec.muttley.disciplina;

import com.fatec.muttley.professor.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< Updated upstream
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/disciplinas")
=======
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
>>>>>>> Stashed changes
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;
<<<<<<< Updated upstream

    @Autowired
    private DisciplinaMapper disciplinaMapper;

    @GetMapping
    public ResponseEntity<List<Disciplina>> listarTodas() {
        return ResponseEntity.ok(disciplinaService.procurarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoDisciplina> buscarPorId(@PathVariable Long id) {
        Disciplina disciplina = disciplinaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
        return ResponseEntity.ok(disciplinaMapper.toAtualizacaoDto(disciplina));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoDisciplina dto) {
        Disciplina disciplinaSalva = disciplinaService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Disciplina '" + disciplinaSalva.getNome() + "' criada com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoDisciplina dto) {
        disciplinaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
        Disciplina disciplinaSalva = disciplinaService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Disciplina '" + disciplinaSalva.getNome() + "' atualizada com sucesso."));
=======

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
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
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
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
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
>>>>>>> Stashed changes
    }

    @DeleteMapping("/{id}")
    @Transactional
<<<<<<< Updated upstream
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        disciplinaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
        disciplinaService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Disciplina " + id + " deletada com sucesso."));
=======
    public String deletarDisciplina(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            disciplinaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "A disciplina " + id + " foi deletada.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/admin/disciplinas";
>>>>>>> Stashed changes
    }
}