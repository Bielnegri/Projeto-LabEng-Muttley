package com.fatec.muttley.professor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "professor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Professor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "professor_id")
    private long id;
    private String nome;
    private String email;
    private String formacao;

    public Professor(AtualizacaoProfessor dados){
        this.nome = dados.nome();
        this.email = dados.email();
        this.formacao = dados.formacao();
    }

    public void AtualizarInformações(AtualizacaoProfessor dados){
        if (dados.nome() != null)
            this.nome = dados.nome();
        if (dados.email() != null)
            this.email = dados.email();
        if (dados.formacao() != null)
            this.formacao = dados.formacao();
    }
}
