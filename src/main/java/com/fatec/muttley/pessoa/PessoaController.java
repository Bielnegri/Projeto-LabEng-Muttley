package com.fatec.muttley.pessoa;

import com.fatec.muttley.aluno.AlunoService;
import com.fatec.muttley.colaborador.ColaboradorService;
import com.fatec.muttley.organizador.OrganizadorService;
import com.fatec.muttley.palestrante.PalestranteService;
import com.fatec.muttley.professor.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< Updated upstream
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PessoaController {

=======
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PessoaController {
>>>>>>> Stashed changes
    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private PalestranteService palestranteService;

    @Autowired
    private OrganizadorService organizadorService;

    @Autowired
    private ColaboradorService colaboradorService;

<<<<<<< Updated upstream
    @GetMapping("/api/admin/alunos")
    public ResponseEntity<List<?>> listarAlunos() {
        return ResponseEntity.ok(alunoService.procurarTodos());
    }

    @GetMapping("/api/admin/professores")
    public ResponseEntity<List<?>> listarProfessores() {
        return ResponseEntity.ok(professorService.procurarTodos());
    }

    @GetMapping("/api/admin/palestrantes")
    public ResponseEntity<List<?>> listarPalestrantes() {
        return ResponseEntity.ok(palestranteService.procurarTodos());
    }

    @GetMapping("/api/admin/organizadores")
    public ResponseEntity<List<?>> listarOrganizadores() {
        return ResponseEntity.ok(organizadorService.procurarTodos());
    }

    @GetMapping("/api/admin/colaboradores")
    public ResponseEntity<List<?>> listarColaboradores() {
        return ResponseEntity.ok(colaboradorService.procurarTodos());
    }

    @GetMapping("/api/pessoas")
    public ResponseEntity<List<Pessoa>> listarTodos() {
        return ResponseEntity.ok(pessoaService.procurarTodos());
    }

    @GetMapping("/api/pessoas/{id}")
    public ResponseEntity<AtualizacaoPessoa> buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        return ResponseEntity.ok(pessoaMapper.toAtualizacaoDto(pessoa));
    }

    @PostMapping("/api/pessoas")
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoPessoa dto) {
        Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Pessoa '" + pessoaSalva.getNome() + "' criada com sucesso!"));
    }

    @PutMapping("/api/pessoas/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoPessoa dto) {
        pessoaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Pessoa '" + pessoaSalva.getNome() + "' atualizada com sucesso!"));
    }

    @DeleteMapping("/api/pessoas/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        pessoaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        pessoaService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Pessoa " + id + " foi apagada!"));
    }
}
=======
    @GetMapping("/admin/aluno")
    public String listarAlunos(Model model) {
        model.addAttribute("alunos", alunoService.procurarTodos());
        return "admin/aluno/listagem";
    }

    @GetMapping("/admin/professor")
    public String listarProfessores(Model model) {
        model.addAttribute("professores", professorService.procurarTodos());
        return "admin/professor/listagem";
    }

    @GetMapping("/admin/palestrante")
    public String listarPalestrantes(Model model) {
        model.addAttribute("palestrantes", palestranteService.procurarTodos());
        return "admin/palestrante/listagem";
    }

    @GetMapping("/admin/organizador")
    public String listarOrganizadores(Model model) {
        model.addAttribute("organizadores", organizadorService.procurarTodos());
        return "admin/organizador/listagem";
    }

    @GetMapping("/admin/colaborador")
    public String listarColaboradores(Model model) {
        model.addAttribute("colaboradores", colaboradorService.procurarTodos());
        return "admin/colaborador/listagem";
    }

    @GetMapping("/pessoa/listagem")
    public String carregaPaginaFormulario (Model model){
        //devolver DTO
        model.addAttribute("listaPessoas", pessoaService.procurarTodos());
        return "pessoa/listagem";
    }

    @GetMapping("/pessoa/formulario")
    public String mostrarFormulario (@RequestParam(required = false) Long id, Model model) {
        AtualizacaoPessoa dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Pessoa pessoa = pessoaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
            dto = pessoaMapper.toAtualizacaoDto(pessoa);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoPessoa(null, "", "", "", "", "");
        }
        model.addAttribute("pessoa", dto);
        return "pessoa/formulario";
    }

    @GetMapping ("/pessoa/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoPessoa dto;
        try {
            if(id != null) {
                Pessoa pessoa = pessoaService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Pessoa não encontrada"));
                //mapear pessoa para AtualizacaoPessoa
                dto = pessoaMapper.toAtualizacaoDto(pessoa);
                model.addAttribute("pessoa", dto);
            }
            return "pessoa/formulario";
        } catch (EntityNotFoundException e) {
            //resolver erros
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pessoa/formulario";
        }
    }

    @PostMapping("/pessoa/salvar")
    public String salvar(@ModelAttribute("pessoa") @Valid AtualizacaoPessoa dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            // Recarrega dados necessários para mostrar erros
            return "pessoa/formulario";
        }
        try {
            Pessoa pessoaSalvo = pessoaService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Pessoa '" + pessoaSalvo.getNome() + "' atualizada com sucesso!"
                    : "Pessoa '" + pessoaSalvo.getNome() + "' criada com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/pessoa/listagem";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/pessoa/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/pessoa/delete/{id}")
    @Transactional
    public String deletarPessoa(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            pessoaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "A pessoa " + id + " foi apagada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/pessoa/listagem";
    }
}
>>>>>>> Stashed changes
