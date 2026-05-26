package com.fatec.muttley;

import com.fatec.muttley.pessoa.AtualizacaoPessoa;
import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PessoaService pessoaService;

    public record LoginRequest(String cpf, String senha) {}

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid AtualizacaoPessoa dto) {
        try {
            if (pessoaService.procurarPorCpf(dto.cpf()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Usuário já cadastrado com esse CPF."));
            }
            Pessoa pessoaSalva = pessoaService.salvarOuAtualizar(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuário '" + pessoaSalva.getNome() + "' cadastrado com sucesso."));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> validarCredenciais(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<Pessoa> pessoaOptional = pessoaService.procurarPorCpf(loginRequest.cpf());

            if (pessoaOptional.isEmpty() || !Objects.equals(loginRequest.senha(), pessoaOptional.get().getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "CPF ou senha incorretos"));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Login efetuado com sucesso",
                    "usuario", pessoaOptional.get().getNome()
            ));

        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", exception.getMessage()));
        }
    }
}