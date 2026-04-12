package com.fatec.muttley.participante;

public class Participante {
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
