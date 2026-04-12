package com.fatec.muttley.medalha;

import jakarta.persistence.Id;

public class Medalha {
    @Id
    private long id;
    private String nome;
    private String descricao;

    public Medalha() {
        super();
    }
    public Medalha(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
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
    
    public void setId(long id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String toString() {
        return nome + "\n" + descricao;
    }
}
