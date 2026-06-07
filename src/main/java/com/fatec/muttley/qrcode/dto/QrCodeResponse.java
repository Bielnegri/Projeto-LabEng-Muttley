package com.fatec.muttley.qrcode.dto;

public record QrCodeResponse(
        Long eventoId,
        String qrCodeUrl,
        String status,
        String errorMessage,
        TipoQrCode tipo
) {}