package com.fatec.muttley.pessoa;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Pessoa salvarOuAtualizar(AtualizacaoPessoa dto) {
        if (dto.id() != null) {
            Pessoa existente = pessoaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa nao encontrada com ID: " + dto.id()));
            String senhaAtual = existente.getSenha();
            pessoaMapper.updateEntityFromDto(dto, existente);
            if (dto.senha() == null || dto.senha().isBlank()) {
                existente.setSenha(senhaAtual);
            } else {
                existente.setSenha(passwordEncoder.encode(dto.senha()));
            }
            return pessoaRepository.save(existente);
        }

        Pessoa novaPessoa = pessoaMapper.toEntityFromAtualizacao(dto);
        novaPessoa.setSenha(passwordEncoder.encode(dto.senha()));
        if (novaPessoa.getRole() == null) {
            novaPessoa.setRole(Role.USER);
        }
        return pessoaRepository.save(novaPessoa);
    }

    public Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> procurarTodos() {
        return pessoaRepository.findAll(Sort.by("nome").ascending());
    }

    public void apagarPorId(Long id) {
        pessoaRepository.deleteById(id);
    }

    public Optional<Pessoa> procurarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    public Optional<Pessoa> procurarPorCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf);
    }

    public Optional<Pessoa> procurarPorEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }

    public boolean existePorEmail(String email) {
        return pessoaRepository.existsByEmail(email);
    }

    public long contar() {
        return pessoaRepository.count();
    }

    public boolean existeAdmin() {
        return pessoaRepository.existsByRole(Role.ADMIN);
    }
}
