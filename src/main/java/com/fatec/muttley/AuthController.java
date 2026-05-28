package com.fatec.muttley;

import com.fatec.muttley.pessoa.AtualizacaoPessoa;
import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaService;
import com.fatec.muttley.pessoa.Role;
import com.fatec.muttley.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public record LoginRequest(
            @NotBlank(message = "Email e obrigatorio")
            @Email(message = "Email invalido")
            String email,

            @NotBlank(message = "Senha e obrigatoria")
            String senha
    ) {
    }

    public record UsuarioResponse(Long id, String nome, String email, Role role) {
    }

    public record LoginResponse(String accessToken, String tokenType, long expiresIn, UsuarioResponse usuario) {
    }

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid AtualizacaoPessoa dto) {
        try {
            if (pessoaService.existePorEmail(dto.email())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Usuario ja cadastrado com esse email."));
            }

            boolean criarAdmin = !pessoaService.existeAdmin();
            Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
            pessoaSalva.setRole(criarAdmin ? Role.ADMIN : Role.USER);
            pessoaSalva = pessoaService.salvar(pessoaSalva);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse(pessoaSalva));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> validarCredenciais(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Optional<Pessoa> pessoaOptional = pessoaService.procurarPorEmail(loginRequest.email());

            if (pessoaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Email ou senha invalidos"));
            }

            Pessoa pessoaSalva = pessoaOptional.get();

            if (!passwordEncoder.matches(loginRequest.senha(), pessoaSalva.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Email ou senha invalidos"));
            }

            if (pessoaSalva.getRole() == null) {
                pessoaSalva.setRole(Role.USER);
                pessoaSalva = pessoaService.salvar(pessoaSalva);
            }

            return ResponseEntity.ok(new LoginResponse(
                    jwtService.gerarToken(pessoaSalva),
                    "Bearer",
                    jwtService.getExpirationSeconds(),
                    usuarioResponse(pessoaSalva)
            ));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", exception.getMessage()));
        }
    }

    private UsuarioResponse usuarioResponse(Pessoa pessoa) {
        return new UsuarioResponse(pessoa.getId(), pessoa.getNome(), pessoa.getEmail(), pessoa.getRole());
    }
}
