package com.fatec.muttley.certificado;

import com.fatec.muttley.evento.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
}