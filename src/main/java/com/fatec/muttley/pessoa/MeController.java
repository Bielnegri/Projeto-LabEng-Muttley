package com.fatec.muttley.pessoa;

import com.fatec.muttley.certificado.CertificadoService;
import com.fatec.muttley.medalha.MedalhaService;
import com.fatec.muttley.participacao.ParticipacaoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MeController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private MedalhaService medalhaService;

    @Autowired
    private ParticipacaoService participacaoService;

    @GetMapping("/api/me")
    public ResponseEntity<PessoaMeResponse> buscarUsuarioAutenticado(JwtAuthenticationToken authentication) {
        Pessoa pessoa = pessoaAutenticada(authentication);

        return ResponseEntity.ok(PessoaMeResponse.from(pessoa));
    }

    @GetMapping("/api/me/certificados")
    public ResponseEntity<List<CertificadoUsuarioResponse>> listarCertificados(JwtAuthenticationToken authentication) {
        Pessoa pessoa = pessoaAutenticada(authentication);
        List<CertificadoUsuarioResponse> certificados = certificadoService.procurarPorPessoa(pessoa.getId()).stream()
                .map(CertificadoUsuarioResponse::from)
                .toList();
        return ResponseEntity.ok(certificados);
    }

    @GetMapping("/api/me/medalhas")
    public ResponseEntity<List<MedalhaUsuarioResponse>> listarMedalhas(JwtAuthenticationToken authentication) {
        Pessoa pessoa = pessoaAutenticada(authentication);
        List<MedalhaUsuarioResponse> medalhas = medalhaService.procurarPorPessoa(pessoa.getId()).stream()
                .map(MedalhaUsuarioResponse::from)
                .toList();
        return ResponseEntity.ok(medalhas);
    }

    @GetMapping("/api/me/participacoes")
    public ResponseEntity<List<ParticipacaoUsuarioResponse>> listarParticipacoes(JwtAuthenticationToken authentication) {
        Pessoa pessoa = pessoaAutenticada(authentication);
        List<ParticipacaoUsuarioResponse> participacoes = participacaoService.procurarPorPessoa(pessoa.getId()).stream()
                .map(ParticipacaoUsuarioResponse::from)
                .toList();
        return ResponseEntity.ok(participacoes);
    }

    private Pessoa pessoaAutenticada(JwtAuthenticationToken authentication) {
        String email = authentication.getToken().getSubject();
        return pessoaService.procurarPorEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario autenticado nao encontrado."));
    }
}
