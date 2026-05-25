package com.fatec.muttley.endereco;

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
@RequestMapping("/api/admin/enderecos")
public class EnderecoController {

=======
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/admin/locais/enderecos")
public class EnderecoController {
>>>>>>> Stashed changes
    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private EnderecoMapper enderecoMapper;

<<<<<<< Updated upstream
    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos() {
        return ResponseEntity.ok(enderecoService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoEndereco> buscarPorId(@PathVariable Long id) {
        Endereco endereco = enderecoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado."));
        return ResponseEntity.ok(enderecoMapper.toAtualizacaoDto(endereco));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoEndereco dto) {
        Endereco enderecoSalvo = enderecoService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Endereço '" + enderecoSalvo.getLogradouro() + "' criado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoEndereco dto) {
        enderecoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado."));
        Endereco enderecoSalvo = enderecoService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Endereço '" + enderecoSalvo.getLogradouro() + "' atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        enderecoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado."));
        enderecoService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Endereço " + id + " foi apagado!"));
    }
}
=======
    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoEndereco dto;
        if (id != null) {
            Endereco endereco = enderecoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Endereco nao encontrado"));
            dto = enderecoMapper.toAtualizacaoDto(endereco);
        } else {
            dto = new AtualizacaoEndereco(null, "", "", "", "", 0, "");
        }

        model.addAttribute("endereco", dto);
        return "admin/locais/endereco-formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("endereco") @Valid AtualizacaoEndereco dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/locais/endereco-formulario";
        }

        try {
            Endereco enderecoSalvo = enderecoService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Endereco '" + enderecoSalvo.getLogradouro() + "' atualizado com sucesso!"
                    : "Endereco '" + enderecoSalvo.getLogradouro() + "' criado com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/admin/locais";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/locais/enderecos/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarEndereco(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            enderecoService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O endereco " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/locais";
    }
}
>>>>>>> Stashed changes
