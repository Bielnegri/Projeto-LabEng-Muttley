package com.fatec.muttley.participante;

import com.fatec.muttley.aluno.AlunoService;
import com.fatec.muttley.evento.EventoService;
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
@RequestMapping("/participante")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    @Autowired
    private ParticipanteMapper participanteMapper;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private EventoService eventoService;

    @GetMapping("/listagem")
    public String carregaPaginaListagem(Model model) {
        model.addAttribute("listaParticipantes", participanteService.procurarTodos());
        return "participante/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario(@RequestParam(required = false) Long id, Model model) {
        AtualizacaoParticipante dto;
        if (id != null) {
            Participante participante = participanteService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Participante nao encontrado."));
            dto = participanteMapper.toAtualizacaoDto(participante);
        } else {
            dto = new AtualizacaoParticipante(null, 0, null, null);
        }
        model.addAttribute("participante", dto);
        model.addAttribute("alunos", alunoService.procurarTodos());
        model.addAttribute("eventos", eventoService.procurarTodos());
        return "participante/formulario";
    }

    @GetMapping("/formulario/{id}")
    public String carregaFormularioPorId(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Participante participante = participanteService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Participante nao encontrado."));
            AtualizacaoParticipante dto = participanteMapper.toAtualizacaoDto(participante);
            model.addAttribute("participante", dto);
            model.addAttribute("alunos", alunoService.procurarTodos());
            model.addAttribute("eventos", eventoService.procurarTodos());
            return "participante/formulario";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/participante/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("participante") @Valid AtualizacaoParticipante dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("alunos", alunoService.procurarTodos());
            model.addAttribute("eventos", eventoService.procurarTodos());
            return "participante/formulario";
        }
        try {
            Participante participanteSalvo = participanteService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Participante '" + participanteSalvo.getInscricao() + "' foi alterado com sucesso."
                    : "Participante '" + participanteSalvo.getInscricao() + "' criado com sucesso.";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/participante/listagem";
        } catch (EntityNotFoundException exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
            return "redirect:/participante/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarParticipante(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            participanteService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "O id: " + id + "Foi deletado com sucesso");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("erro", exception.getMessage());
        }
        return "redirect:/participante/listagem";
    }
}
