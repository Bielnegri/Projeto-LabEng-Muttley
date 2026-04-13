package com.fatec.muttley.certificado;

import com.fatec.muttley.aluno.AtualizacaoAluno;
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
@RequestMapping("/certificado")
public class CertificadoController {
    @Autowired
    private CertificadoService certificadoService;
    @Autowired
    private CertificadoMapper certificadoMapper;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario (Model model){
        model.addAttribute("listaCertificados", certificadoService.procurarTodos());
        return "certificado/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario (@RequestParam(required = false) Long id, Model model){
        AtualizacaoCertificado dto;
        if (id != null){
            Certificado certificado = certificadoService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));
            dto = certificadoMapper.toAtualizacaoDto(certificado);
        } else {
            dto = new AtualizacaoCertificado(null, null, "");
        }
        model.addAttribute("aluno", dto);
        return "certificado/formulario";
    }

    @GetMapping("/formulario/{îd}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        AtualizacaoCertificado dto;
        try {
            if(id != null){
                Certificado certificado = certificadoService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Certificado não encontrado."));
                dto = certificadoMapper.toAtualizacaoDto(certificado);
                model.addAttribute("certificado", dto);
            }
            return "certificado/formulario";
        } catch (EntityNotFoundException exception){
            redirectAttributes.addFlashAttribute("Erro", exception.getMessage());
            return "redirect:/certificado/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("certificado") @Valid AtualizacaoCertificado dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (){

        }
        try {

        } catch (){

        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deletarCertificado(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        try {

        }catch (){

        }
    }
}
