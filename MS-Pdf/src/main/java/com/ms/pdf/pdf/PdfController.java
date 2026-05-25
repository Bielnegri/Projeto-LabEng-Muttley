package com.ms.pdf.pdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PdfController {

    @Autowired
    private PdfService pdfService;

    public record GerarPdfRequest(String htmlContent) {}

    @PostMapping("api/pdf/gerar")
    public ResponseEntity<?> gerarPdf(@RequestBody GerarPdfRequest request) {
        try {
            byte[] pdfBytes = pdfService.generatePdfFromHtml(request.htmlContent());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "documento.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao gerar PDF: " + e.getMessage()));
        }
    }
}