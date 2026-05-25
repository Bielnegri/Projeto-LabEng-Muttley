package com.fatec.muttley.qrcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @GetMapping("/url")
    public ResponseEntity<Map<String, String>> obterUrlQrCode(
            @RequestParam String baseUrl,
            @RequestParam Long eventoId,
            @RequestParam String eventoTema) {

        String urlGerada = qrCodeService.gerarUrlQrCode(baseUrl, eventoId, eventoTema);
        return ResponseEntity.ok(Map.of("qrCodeUrl", urlGerada));
    }

    @GetMapping(value = "/baixar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> baixarImagemQrCode(
            @RequestParam String baseUrl,
            @RequestParam Long eventoId,
            @RequestParam String eventoTema) {
        try {
            String urlGerada = qrCodeService.gerarUrlQrCode(baseUrl, eventoId, eventoTema);
            byte[] imagemQr = qrCodeService.baixarQrCode(urlGerada);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imagemQr);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao baixar QR Code: " + e.getMessage()));
        }
    }
}