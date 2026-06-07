package com.fatec.muttley.qrcode.dto;

public record QrCodeRequest(
        Long eventoId,
        String baseUrl,
        String tema,
        TipoQrCode tipo
) {}