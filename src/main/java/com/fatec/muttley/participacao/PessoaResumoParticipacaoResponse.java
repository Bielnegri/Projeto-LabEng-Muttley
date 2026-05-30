package com.fatec.muttley.participacao;

import com.fatec.muttley.pessoa.Pessoa;

public record PessoaResumoParticipacaoResponse(
        Long id,
        String nome,
        String email,
        String cpf
) {
    public static PessoaResumoParticipacaoResponse from(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new PessoaResumoParticipacaoResponse(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getCpf()
        );
    }
}
