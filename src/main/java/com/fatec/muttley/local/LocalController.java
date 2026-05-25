package com.fatec.muttley.local;

<<<<<<< Updated upstream
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
@RequestMapping("/api/admin/locais")
public class LocalController {

=======
import com.fatec.muttley.endereco.EnderecoService;
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
@RequestMapping("/admin/locais")
public class LocalController {
>>>>>>> Stashed changes
    @Autowired
    private LocalService localService;

    @Autowired
<<<<<<< Updated upstream
    private LocalMapper localMapper;

    @GetMapping
    public ResponseEntity<List<Local>> listarTodos() {
        return ResponseEntity.ok(localService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoLocal> buscarPorId(@PathVariable Long id) {
        Local local = localService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado."));
        return ResponseEntity.ok(localMapper.toAtualizacaoDto(local));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoLocal dto) {
        Local localSalvo = localService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Local '" + localSalvo.getNome() + "' criado com sucesso!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoLocal dto) {
        localService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado."));
        Local localSalvo = localService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Local '" + localSalvo.getNome() + "' atualizado com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        localService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Local não encontrado."));
        localService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Local " + id + " foi apagado!"));
    }
}
=======
    private EnderecoService enderecoService;

    @Autowired
    private LocalMapper localMapper;

    @GetMapping({"", "/listagem"})
    public String carregaPaginaFormulario (Model model){
        //devolver DTO
        model.addAttribute("listaLocais", localService.procurarTodos());
        model.addAttribute("listaEnderecos", enderecoService.procurarTodos());
        return "admin/locais/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoLocal dto;
        if (id != null) {
            //edição: Carrega dados existentes
            Local local = localService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Local não encontrado"));
            dto = localMapper.toAtualizacaoDto(local);
        } else {
            // criação: DTO vazio
            dto = new AtualizacaoLocal(null, "", "", 0, null);
        }
        model.addAttribute("local", dto);
        model.addAttribute("enderecos", enderecoService.procurarTodos());
        return "admin/locais/formulario";
    }
    @GetMapping ("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoLocal dto;
        try {
            if(id != null) {
                Local local = localService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Local não encontrado"));
                model.addAttribute("enderecos", enderecoService.procurarTodos());
                //mapear local para AtualizacaoLocal
                dto = localMapper.toAtualizacaoDto(local);
                model.addAttribute("local", dto);
            }
            return "admin/locais/formulario";
        } catch (EntityNotFoundException e) {
            //resolver erros
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/locais/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("local") @Valid AtualizacaoLocal dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            // Recarrega dados necessários para mostrar erros
            model.addAttribute("enderecos", enderecoService.procurarTodos());
            return "admin/locais/formulario";
        }
        try {
            Local localSalvo = localService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Local '" + localSalvo.getNome() + "' atualizado com sucesso!"
                    : "Local '" + localSalvo.getNome() + "' criado com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/admin/locais";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/locais/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarLocal(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            localService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O local " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/locais";
    }
}
>>>>>>> Stashed changes
