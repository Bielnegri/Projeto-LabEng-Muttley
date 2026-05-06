package com.fatec.muttley.participacao;

import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.pessoa.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/participacao")
public class ParticipacaoController {

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private ParticipacaoMapper participacaoMapper;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EventoService eventoService;

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
                    .orElseThrow(() -> new EntityNotFoundException("Participacao nao encontrada."));
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
                    .orElseThrow(() -> new EntityNotFoundException("Participacao nao encontrada."));
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
            String mensagem = dto.id() != null
                    ? "Participacao '" + participacaoSalvo.getInscricao() + "' foi alterada com sucesso."
                    : "Participacao '" + participacaoSalvo.getInscricao() + "' criada com sucesso.";
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
