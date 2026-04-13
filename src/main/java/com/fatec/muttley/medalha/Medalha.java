package com.fatec.muttley.medalha;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fatec.muttley.aluno.Aluno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medalha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Medalha {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "medalha_id")
    private Long id;
    private String nome;
    private String descricao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", referencedColumnName = "aluno_id")
    private Aluno aluno;

    public Medalha(AtualizacaoMedalha dados, Aluno aluno) {
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.aluno = aluno;
    }

    public void atualizarInformacoes(AtualizacaoMedalha dados, Aluno aluno) {
        if (dados.nome() != null)
            this.nome = dados.nome();
        if (dados.descricao() != null)
            this.descricao = dados.descricao();
        if (aluno != null)
            this.aluno = aluno;
    }
}
