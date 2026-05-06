package com.fatec.muttley.certificado;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record AtualizacaoCertificado(
        Long id,

        @NotNull(message = "A data de emissão é obrigatória")
        @PastOrPresent(message = "A data não pode ser futura")
        Date dataEmissao,

        @NotBlank(message = "Assinatura é obrigatória")
        String assinatura
) {

}
