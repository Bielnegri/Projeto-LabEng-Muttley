package com.fatec.muttley.disciplina;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.professor.Professor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplina")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private Long id;

    private String nome;
    private String descricao;
    private String turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor", referencedColumnName = "id_professor")
    private Professor professor;

    @OneToMany(mappedBy = "disciplina")
    private List<Evento> eventos = new ArrayList<>();

    public Disciplina(AtualizacaoDisciplina dados, Professor professor) {
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.turno = dados.turno();
        this.professor = professor;
    }

    public void atualizarInformacoes(AtualizacaoDisciplina dados, Professor professor) {
        if (dados.nome() != null)
            this.nome = dados.nome();
        if (dados.descricao() != null)
            this.descricao = dados.descricao();
        if (dados.turno() != null)
            this.turno = dados.turno();
        if (professor != null)
            this.professor = professor;
    }
}
