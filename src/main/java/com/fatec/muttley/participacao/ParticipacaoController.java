package com.fatec.muttley.participacao;

import com.fatec.muttley.email.EmailService;
<<<<<<< Updated upstream
=======
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.pessoa.PessoaService;
>>>>>>> Stashed changes
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
@RequestMapping("/api/participacoes")
=======
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/participacao")
>>>>>>> Stashed changes
public class ParticipacaoController {

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private ParticipacaoMapper participacaoMapper;

    @Autowired
<<<<<<< Updated upstream
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<Participacao>> listarTodos() {
        return ResponseEntity.ok(participacaoService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoParticipacao> buscarPorId(@PathVariable Long id) {
        Participacao participacao = participacaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
        return ResponseEntity.ok(participacaoMapper.toAtualizacaoDto(participacao));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoParticipacao dto) {
        Participacao participacaoSalva = participacaoService.salvarOuAtualizar(dto);
        emailService.enviarConfirmacaoCadastro(participacaoSalva);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Participação '" + participacaoSalva.getInscricao() + "' criada com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoParticipacao dto) {
        participacaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
        Participacao participacaoSalva = participacaoService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Participação '" + participacaoSalva.getInscricao() + "' alterada com sucesso."));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        participacaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
        participacaoService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Participação " + id + " deletada com sucesso."));
    }
}
=======
    private PessoaService pessoaService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("listaParticipacoes", participacaoService.procurarTodos());
        return "participacao/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoParticipacao dto;
        if (id != null) {
            Participacao participacao = participacaoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
            dto = participacaoMapper.toAtualizacaoDto(participacao);
        } else {
            dto = new AtualizacaoParticipacao(null, 0, null, null, null);
        }
        model.addAttribute("participacao", dto);
        model.addAttribute("pessoas", pessoaService.procurarTodos());
        model.addAttribute("eventos", eventoService.procurarTodos());
        return "participacao/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaFormularioPorId(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Participacao participacao = participacaoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Participação não encontrada."));
            AtualizacaoParticipacao dto = participacaoMapper.toAtualizacaoDto(participacao);
            model.addAttribute("participacao", dto);
            model.addAttribute("pessoas", pessoaService.procurarTodos());
            model.addAttribute("eventos", eventoService.procurarTodos());
            return "participacao/formulario";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/participacao/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("participacao") @Valid AtualizacaoParticipacao dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pessoas", pessoaService.procurarTodos());
            model.addAttribute("eventos", eventoService.procurarTodos());
            return "participacao/formulario";
        }
        try {
            Participacao participacaoSalvo = participacaoService.salvarOuAtualizar(dto);
            if (dto.id() == null) {
                emailService.enviarConfirmacaoCadastro(participacaoSalvo);
            }
            String mensagem = dto.id() != null
                    ? "Participação '" + participacaoSalvo.getInscricao() + "' foi alterada com sucesso."
                    : "Participação '" + participacaoSalvo.getInscricao() + "' criada com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/participacao/listagem";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/participacao/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarParticipacao(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            participacaoService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O id: " + id + "Foi deletada com sucesso");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/participacao/listagem";
    }
}
>>>>>>> Stashed changes
