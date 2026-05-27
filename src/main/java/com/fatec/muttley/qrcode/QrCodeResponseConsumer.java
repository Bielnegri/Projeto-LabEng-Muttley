package com.fatec.muttley.qrcode;

import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.qrcode.dto.QrCodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QrCodeResponseConsumer {

    private final EventoService eventoService;

    @KafkaListener(topics = "qrcode.gerar.response", groupId = "muttley-group")
    public void consumir(QrCodeResponse dto) {
        if ("ERROR".equals(dto.status())) {
            log.error("Falha ao gerar QR Code: eventoId={}, erro={}", dto.eventoId(), dto.errorMessage());
            return;
        }

        log.info("QR Code recebido: eventoId={}", dto.eventoId());
        eventoService.salvarQrCodeUrl(dto.eventoId(), dto.qrCodeUrl());
    }
}