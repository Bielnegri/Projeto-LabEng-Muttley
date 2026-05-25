package com.fatec.muttley.endereco;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private EnderecoMapper enderecoMapper;

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
