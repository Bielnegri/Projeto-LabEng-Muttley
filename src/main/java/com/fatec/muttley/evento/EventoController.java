package com.fatec.muttley.evento;

import com.fatec.muttley.disciplina.DisciplinaService;
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
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoMapper eventoMapper;

    @Autowired
    private DisciplinaService disciplinaService;

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("listaEventos", eventoService.procurarTodos());
        return "evento/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoEvento dto;
        if (id != null) {
            Evento evento = eventoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Evento nao encontrado."));
            dto = eventoMapper.toAtualizacaoDto(evento);
        } else {
            dto = new AtualizacaoEvento(null, "", "", null, "", null);
        }
        model.addAttribute("evento", dto);
        model.addAttribute("disciplinas", disciplinaService.procurarTodas());
        return "evento/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaFormularioPorId(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Evento nao encontrado."));
            AtualizacaoEvento dto = eventoMapper.toAtualizacaoDto(evento);
            model.addAttribute("evento", dto);
            model.addAttribute("disciplinas", disciplinaService.procurarTodas());
            return "evento/formulario";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/evento/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("evento") @Valid AtualizacaoEvento dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("disciplinas", disciplinaService.procurarTodas());
            return "evento/formulario";
        }
        try {
            Evento eventoSalvo = eventoService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Evento '" + eventoSalvo.getTema() + "' atualizado com sucesso."
                    : "Evento '" + eventoSalvo.getTema() + "' criado com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/evento/listagem";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/evento/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarEvento(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "Evento: " + id + " cancelado com sucesso.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/evento/listagem";
    }
}
