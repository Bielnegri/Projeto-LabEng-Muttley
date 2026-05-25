package com.fatec.muttley.medalha;

<<<<<<< Updated upstream
=======
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.participacao.ParticipacaoService;
>>>>>>> Stashed changes
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

<<<<<<< Updated upstream
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/medalhas")
=======
@Controller
@RequestMapping("/admin/medalhas")
>>>>>>> Stashed changes
public class MedalhaController {

    @Autowired
    private MedalhaService medalhaService;

    @Autowired
<<<<<<< Updated upstream
    private MedalhaMapper medalhaMapper;

    @GetMapping
    public ResponseEntity<List<Medalha>> listarTodos() {
        return ResponseEntity.ok(medalhaService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtualizacaoMedalha> buscarPorId(@PathVariable Long id) {
        Medalha medalha = medalhaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada."));
        return ResponseEntity.ok(medalhaMapper.toAtualizacaoDto(medalha));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> criar(@RequestBody @Valid AtualizacaoMedalha dto) {
        Medalha medalhaSalva = medalhaService.salvarOuAtualizar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Medalha '" + medalhaSalva.getNome() + "' criada com sucesso!"));
=======
    private ParticipacaoService participacaoService;

    @Autowired
    private MedalhaMapper medalhaMapper;

    @GetMapping({"", "/listagem"})
    public String carregaPaginaFormulario (Model model){
        model.addAttribute("listaMedalhas", medalhaService.procurarTodos());
        return "admin/medalhas/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id,
                                    @RequestParam(required = false) Long participacaoId,
                                    Model model) {
        AtualizacaoMedalha dto;
        if (id != null) {
            Medalha medalha = medalhaService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada"));
            dto = medalhaMapper.toAtualizacaoDto(medalha);
        } else {
            dto = new AtualizacaoMedalha(null, "", "", participacaoId);
        }
        popularFormulario(model, dto);
        return "admin/medalhas/formulario";
    }

    @GetMapping ("/formulario/{id}")
    public String carregaPaginaFormulario (@PathVariable("id") Long id, Model model,
                                           RedirectAttributes redirectAttributes) {
        AtualizacaoMedalha dto;
        try {
            if(id != null) {
                Medalha medalha = medalhaService.procurarPorId(id).orElseThrow(() ->
                        new EntityNotFoundException("Medalha não encontrada"));
                dto = medalhaMapper.toAtualizacaoDto(medalha);
                popularFormulario(model, dto);
            }
            return "admin/medalhas/formulario";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/medalhas/formulario";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("medalha") @Valid AtualizacaoMedalha dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            popularFormulario(model, dto);
            return "admin/medalhas/formulario";
        }
        try {
            Medalha medalhaSalvo = medalhaService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null
                    ? "Medalha '" + medalhaSalvo.getNome() + "' atualizada com sucesso!"
                    : "Medalha '" + medalhaSalvo.getNome() + "' criada com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/admin/medalhas";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/medalhas/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
>>>>>>> Stashed changes
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid AtualizacaoMedalha dto) {
        medalhaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada."));
        Medalha medalhaSalva = medalhaService.salvarOuAtualizar(dto);
        return ResponseEntity.ok(Map.of("message", "Medalha '" + medalhaSalva.getNome() + "' atualizada com sucesso!"));
    }

    @DeleteMapping("/{id}")
    @Transactional
<<<<<<< Updated upstream
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        medalhaService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Medalha não encontrada."));
        medalhaService.apagarPorId(id);
        return ResponseEntity.ok(Map.of("message", "Medalha " + id + " foi apagada!"));
=======
    public String deletarMedalha(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            medalhaService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "A medalha " + id + " foi apagada!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/medalhas";
    }

    private void popularFormulario(Model model, AtualizacaoMedalha dto) {
        model.addAttribute("medalha", dto);
        model.addAttribute("participacoes", participacaoService.procurarTodos());
        Participacao participacaoSelecionada = dto.participacaoId() != null
                ? participacaoService.procurarPorId(dto.participacaoId()).orElse(null)
                : null;
        model.addAttribute("participacaoSelecionada", participacaoSelecionada);
>>>>>>> Stashed changes
    }
}