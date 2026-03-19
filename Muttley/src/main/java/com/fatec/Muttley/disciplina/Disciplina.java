package com.fatec.Muttley.disciplina;

import jakarta.persistence.Id;

public class Disciplina {
    @Id
    private long id;
    private String nome;
    private String descricao;
    private String turno;

    public Disciplina() {
        super();
    }
    public Disciplina(String nome, String descricao, String turno) {
        this.nome = nome;
        this.descricao = descricao;
        this.turno = turno;
    }

    public long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getTurno() {
        return turno;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return nome + "\n" + descricao + "\n" + turno;
    }
}
