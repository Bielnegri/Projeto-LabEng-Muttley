package com.fatec.muttley.config;

import com.fatec.muttley.pessoa.Pessoa;
import com.fatec.muttley.pessoa.PessoaRepository;
import com.fatec.muttley.pessoa.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class MockDataInitializer {

    private final PasswordEncoder passwordEncoder;

    public MockDataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner loadMockData(
            PessoaRepository pessoaRepository
    ) {
        return args -> {
            if (pessoaRepository.count() > 0) {
                return;
            }

            List<Pessoa> pessoas = List.of(
                    pessoaRepository.save(criarPessoa("Ana Souza", "ana.souza@email.com", "(11) 99999-1001", "111.111.111-11"))
            );

            pessoas.get(0).setRole(Role.ADMIN);
            pessoaRepository.save(pessoas.get(0));
        };
    }

    private Pessoa criarPessoa(String nome, String email, String telefone, String cpf) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setEmail(email);
        pessoa.setTelefone(telefone);
        pessoa.setCpf(cpf);
        pessoa.setSenha(passwordEncoder.encode("123456"));
        pessoa.setRole(Role.USER);
        return pessoa;
    }
}
