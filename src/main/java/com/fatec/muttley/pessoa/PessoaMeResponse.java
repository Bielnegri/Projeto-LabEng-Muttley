package com.fatec.muttley.pessoa;

public record PessoaMeResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Role role
) {
    public static PessoaMeResponse from(Pessoa pessoa) {
        return new PessoaMeResponse(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getTelefone(),
                pessoa.getCpf(),
                pessoa.getRole()
        );
    }
}
