package com.fatec.muttley.pessoa;

import com.fatec.muttley.aluno.AlunoService;
import com.fatec.muttley.colaborador.ColaboradorService;
import com.fatec.muttley.organizador.OrganizadorService;
import com.fatec.muttley.palestrante.PalestranteService;
import com.fatec.muttley.professor.ProfessorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class PessoaController {

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

    @GetMapping("/alunos")
    public ResponseEntity<List<?>> listarAlunos() {
        return ResponseEntity.ok(alunoService.procurarTodos());
    }

    @GetMapping("/professores")
    public ResponseEntity<List<?>> listarProfessores() {
        return ResponseEntity.ok(professorService.procurarTodos());
    }

    @GetMapping("/palestrantes")
    public ResponseEntity<List<?>> listarPalestrantes() {
        return ResponseEntity.ok(palestranteService.procurarTodos());
    }

    @GetMapping("/organizadores")
    public ResponseEntity<List<?>> listarOrganizadores() {
        return ResponseEntity.ok(organizadorService.procurarTodos());
    }

    @GetMapping("/colaboradores")
    public ResponseEntity<List<?>> listarColaboradores() {
        return ResponseEntity.ok(colaboradorService.procurarTodos());
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<Pessoa>> listarTodos() {
        return ResponseEntity.ok(pessoaService.procurarTodos());
    }

    @GetMapping("/pessoas/{id}")
    public ResponseEntity<AtualizacaoPessoa> buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        return ResponseEntity.ok(pessoaMapper.toAtualizacaoDto(pessoa));
    }

    @PostMapping("/pessoas")
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoPessoa dto) {
        Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Pessoa '" + pessoaSalva.getNome() + "' criada com sucesso!"));
    }

    @PutMapping("/pessoas/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoPessoa dto) {
        pessoaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Pessoa '" + pessoaSalva.getNome() + "' atualizada com sucesso!"));
    }

    @DeleteMapping("/pessoas/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        pessoaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));
        pessoaService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Pessoa " + id + " foi apagada!"));
    }
}