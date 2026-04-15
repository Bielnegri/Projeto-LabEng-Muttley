package com.fatec.muttley.participante;

import jakarta.persistence.*;

@Entity
@Table(name = "participante")
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participante")
    private long id;
    private int inscricao;

    public Participante() {
        super();
    }
    public Participante(int inscricao) {
        this.inscricao = inscricao;
    }
    
    public long getId() {
        return id;
    }
    public int getInscricao() {
        return inscricao;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setInscricao(int inscricao) {
        this.inscricao = inscricao;
    }

    @Override
    public String toString() {
        return String.valueOf(inscricao);
    }
}
