package com.fatec.muttley;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/inicio")
public class InicioController {

    @GetMapping
    public ResponseEntity<Map<String, String>> carregarIndex() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "A API Muttley está online e a funcionar!"
        ));
    }
}