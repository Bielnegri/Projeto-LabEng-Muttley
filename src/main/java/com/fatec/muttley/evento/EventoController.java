package com.fatec.muttley.evento;

import com.fatec.muttley.disciplina.DisciplinaService;
import com.fatec.muttley.local.LocalService;
import com.fatec.muttley.participacao.ParticipacaoService;
import com.fatec.muttley.patrocinador.PatrocinadorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoMapper eventoMapper;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private PatrocinadorService patrocinadorService;

    @Autowired
    private LocalService localService;

    @Autowired
    private ParticipacaoService participacaoService;

    @GetMapping("/eventos/{id_evento}")
    public String carregarPaginaEvento(@PathVariable("id_evento") Long idEvento, Model model) {
        Evento evento = eventoService.procurarPorId(idEvento)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nÃ£o encontrado."));

        model.addAttribute("evento", evento);
        model.addAttribute("inscricoesEncerradas", inscricoesEncerradas(evento));
        return "user/evento/detalhe";
    }

    @GetMapping("/admin/eventos")
    public String carregarEventos(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "data") String ordenar,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            Model model) {

        Sort sort = ordenar.equals("tema")
                ? Sort.by("tema").ascending()
                : Sort.by("data").ascending().and(Sort.by("horarioInicio").ascending());

        Pageable pageable = PageRequest.of(pagina, tamanho, sort);
        Page<Evento> paginaEventos = eventoService.procurarProximosFiltrados(busca, pageable);

        model.addAttribute("paginaEventos", paginaEventos);
        model.addAttribute("busca", busca);
        model.addAttribute("ordenar", ordenar);
        model.addAttribute("tamanho", tamanho);
        return "admin/eventos/eventos";
    }

    @GetMapping("/admin/novoEvento")
    public String carregarFormularioEvento(@RequestParam(required = false) Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoEvento dto;
        if (id != null) {
            try {
                Evento evento = eventoService.procurarPorId(id)
                        .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));
                dto = eventoMapper.toAtualizacaoDto(evento);
            } catch (EntityNotFoundException e) {
                redirectAttributes.addFlashAttribute("erro", e.getMessage());
                return "redirect:/admin/novoEvento";
            }
        } else {
            dto = new AtualizacaoEvento(null, "", null, "", "", "", null, null, null);
        }
        popularFormulario(model, dto);
        return "admin/eventos/formEvento";
    }

    @PostMapping("/admin/salvar")
    public String salvar(@ModelAttribute("evento") @Valid AtualizacaoEvento dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            popularFormulario(model, dto);
            return "admin/eventos/formEvento";
        }
        try {
            Evento eventoSalvo = eventoService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Evento '" + eventoSalvo.getTema() + "' atualizado com sucesso."
                    : "Evento '" + eventoSalvo.getTema() + "' criado com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            String suffix = dto.id() != null ? "?id=" + dto.id() : "";
            return "redirect:/admin/novoEvento" + suffix;
        }
        return "redirect:/admin/eventos";
    }

    @GetMapping("/admin/deletar/{id}")
    @Transactional
    public String deletarEvento(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "Evento cancelado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/eventos";
    }

    private void popularFormulario(Model model, AtualizacaoEvento dto) {
        model.addAttribute("evento", dto);
        model.addAttribute("disciplinas", disciplinaService.procurarTodas());
        model.addAttribute("patrocinadores", patrocinadorService.procurarTodos());
        model.addAttribute("locais", localService.procurarTodos());
        model.addAttribute("participacoes", dto.id() != null
                ? participacaoService.procurarPorEvento(dto.id())
                : java.util.List.of());
    }

    private boolean inscricoesEncerradas(Evento evento) {
        if (evento.getData() == null || evento.getHorarioInicio() == null || evento.getHorarioInicio().isBlank()) {
            return false;
        }

        try {
            LocalDateTime inicioEvento = LocalDateTime.of(
                    evento.getData().toLocalDate(),
                    LocalTime.parse(evento.getHorarioInicio())
            );
            return !inicioEvento.isAfter(LocalDateTime.now());
        } catch (RuntimeException exception) {
            return false;
        }
    }
}
