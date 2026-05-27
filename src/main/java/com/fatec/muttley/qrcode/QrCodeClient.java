package com.fatec.muttley.qrcode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class QrCodeClient {

    private final RestClient restClient;

    public QrCodeClient(@Value("${qrcode.ms-url}") String qrCodeMsUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(qrCodeMsUrl)
                .build();
    }

    public byte[] baixarQrCode(String qrCodeUrl) {
        return restClient.get()
                .uri("/api/qrcode/baixar?url={url}", qrCodeUrl)
                .retrieve()
                .body(byte[].class);
    }
}