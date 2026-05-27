package com.fatec.muttley.qrcode;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.qrcode.dto.QrCodeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeProducer {

    private final KafkaTemplate<String, QrCodeRequest> kafkaTemplate;
    private static final String TOPIC = "qrcode.gerar.request";

    public void publicarGeracaoQrCode(Evento evento, String baseUrl) {
        var dto = new QrCodeRequest(
                evento.getId(),
                baseUrl,
                evento.getTema()
        );
        kafkaTemplate.send(TOPIC, String.valueOf(evento.getId()), dto);
        log.info("QR Code enfileirado: eventoId={}", evento.getId());
    }
}