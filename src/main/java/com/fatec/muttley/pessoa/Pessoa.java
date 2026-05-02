package com.fatec.muttley.pessoa;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.palestrante.Palestrante;
import com.fatec.muttley.participacao.Participacao;
import com.fatec.muttley.professor.Professor;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String cpf;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Aluno aluno;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Professor professor;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Palestrante palestrante;

    @OneToMany(mappedBy = "pessoa")
    private List<Participacao> participacoes = new ArrayList<>();

    public Pessoa(AtualizacaoPessoa dados){
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
    }

    public void atualizarInformacoes(AtualizacaoPessoa dados) {
        if (dados.nome() != null )
            this.nome = dados.nome();
        if (dados.email() != null)
            this.email = dados.email();
        if (dados.telefone() != null)
            this.telefone = dados.telefone();
        if (dados.cpf() != null)
            this.cpf = dados.cpf();
    }

    public boolean isAluno(){
        return this.aluno != null;
    }

    public boolean isProfessor(){
        return this.professor != null;
    }

    public boolean isPalestrante(){
        return this.palestrante != null;
    }
}
