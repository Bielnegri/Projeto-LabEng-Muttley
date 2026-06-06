package com.fatec.muttley.qrcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatec.muttley.evento.EventoService;
import com.fatec.muttley.qrcode.dto.QrCodeResponse;
import com.fatec.muttley.qrcode.dto.TipoQrCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class QrCodeResponseConsumer {

    private final EventoService eventoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "qrcode.gerar.response", groupId = "muttley-group")
    public void consumir(String payload) throws JsonProcessingException {
        QrCodeResponse dto = objectMapper.readValue(payload, QrCodeResponse.class);

        if ("ERROR".equals(dto.status())) {
            log.error("Falha ao gerar QR Code: eventoId={}, erro={}", dto.eventoId(), dto.errorMessage());
            return;
        }

        log.info("QR Code recebido: eventoId={}, tipo={}", dto.eventoId(), dto.tipo());

        if (dto.tipo() == TipoQrCode.CONFIRMACAO) {
            eventoService.salvarQrCodeConfirmacaoUrl(dto.eventoId(), dto.qrCodeUrl());
        } else {
            eventoService.salvarQrCodeInscricaoUrl(dto.eventoId(), dto.qrCodeUrl());
        }
    }
}