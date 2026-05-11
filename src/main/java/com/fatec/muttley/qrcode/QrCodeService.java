package com.fatec.muttley.qrcode;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class QrCodeService {

    private static final String QUICKCHART_BASE = "https://quickchart.io/qr";

    public String gerarUrlQrCode(String baseUrl, Long eventoId, String eventoTema) {
        String urlCadastro = baseUrl + "/eventos/" + eventoId;
        String encoded = URLEncoder.encode(urlCadastro, StandardCharsets.UTF_8);
        return QUICKCHART_BASE + "?text=" + encoded + "&size=300&margin=2";
    }

    public byte[] baixarQrCode(String qrCodeUrl) throws Exception {
        return new java.net.URL(qrCodeUrl).openStream().readAllBytes();
    }
}