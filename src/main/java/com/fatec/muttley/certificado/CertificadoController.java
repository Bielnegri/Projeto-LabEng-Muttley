package com.fatec.muttley.certificado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/certificado")
public class CertificadoController {
    @Autowired
    private CertificadoService certificadoService;
    @Autowired
}
