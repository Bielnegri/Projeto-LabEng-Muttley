package com.fatec.muttley.disciplina;

import com.fatec.muttley.certificado.CertificadoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/disciplina")
public class DisciplinaController {
    @Autowired
        private DisciplinaService disciplinaService;
    @Autowired
        private DisciplinaMapper disciplinaMapper;
    @Autowired
    private CertificadoService certificadoService;

    @GetMapping("/listagem")
    public String carregaPaginaFormulario(Model model){
        model.addAttribute("listaDisciplinas", certificadoService.procurarTodos());
        return "disciplina/listagem";
    }

    @GetMapping("/formulario")
    public String mostraFormulario (@RequestParam(required = false)Long id, Model model){
        AtualizacaoDisciplina dto;
        if(id != null){
            Disciplina disciplina = disciplinaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada."));
            dto = disciplinaMapper.toAtualizacaoDto(disciplina);
        } else {
            dto = new AtualizacaoDisciplina(null, "", "", "");
        }
    }
}
