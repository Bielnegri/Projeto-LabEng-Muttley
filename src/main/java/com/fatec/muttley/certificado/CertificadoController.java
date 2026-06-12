package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private EventoService eventoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarDados() {
        return ResponseEntity.ok(Map.of(
                "certificados", certificadoService.procurarTodos(),
                "eventosAguardandoCertificado", eventoService.procurarEventosAguardandoEmissaoCertificado(),
                "ultimosCertificados", certificadoService.procurarUltimosEmitidos()
        ));
    }

    @PostMapping("/evento/{eventoId}/upload-assinatura")
    public ResponseEntity<?> salvarAssinaturaPorEvento(
            @PathVariable Long eventoId,
            @RequestParam("file") MultipartFile file) {
        try {
            String diretorioUpload = "uploads/assinaturas/";
            Path caminhoDiretorio = Paths.get(diretorioUpload);

            if (!Files.exists(caminhoDiretorio)) {
                Files.createDirectories(caminhoDiretorio);
            }

            String nomeArquivoUnico = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path caminhoArquivo = caminhoDiretorio.resolve(nomeArquivoUnico);

            Files.copy(file.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

            String caminhoFinal = diretorioUpload + nomeArquivoUnico;

            certificadoService.atualizarAssinaturaPorEvento(eventoId, caminhoFinal);

            return ResponseEntity.ok(Map.of("mensagem", "Assinatura vinculada a todos os certificados do evento!"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("erro", "Falha ao salvar: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/upload-assinatura")
    public ResponseEntity<?> salvarAssinaturaNaPasta(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            String diretorioUpload = "uploads/assinaturas/";
            Path caminhoDiretorio = Paths.get(diretorioUpload);

            if (!Files.exists(caminhoDiretorio)) {
                Files.createDirectories(caminhoDiretorio);
            }

            String nomeArquivoUnico = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path caminhoArquivo = caminhoDiretorio.resolve(nomeArquivoUnico);

            Files.copy(file.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

            String caminhoFinal = diretorioUpload + nomeArquivoUnico;

            certificadoService.atualizarCaminhoAssinatura(id, caminhoFinal);

            return ResponseEntity.ok(Map.of("mensagem", "Assinatura vinculada com sucesso!"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("erro", "Falha ao salvar: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/assinatura-visual")
    public ResponseEntity<Resource> exibirImagemDaPasta(@PathVariable Long id) {
        try {
            Certificado certificado = certificadoService.procurarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

            String caminhoString = certificado.getCaminhoAssinaturaVisual();
            if (caminhoString == null || caminhoString.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path caminhoArquivo = Paths.get(caminhoString);
            Resource recurso = new UrlResource(caminhoArquivo.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}