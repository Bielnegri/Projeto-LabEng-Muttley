package com.fatec.muttley.professor;

public class Professor {
    private long id;
    private String nome;
    private String email;
    private String formacao;

    public Professor() {
        super();
    }
    public Professor(String nome, String email, String formacao) {
        this.nome = nome;
        this.email = email;
        this.formacao = formacao;
    }

    public long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getFormacao() {
        return formacao;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    @Override
    public String toString() {
        return nome + "\n" + email;
    }
    
}
