package com.fatec.Muttley.certificado;

import java.sql.Date;

import jakarta.persistence.Id;

public class Certificado {
    @Id
    private long id;
    private Date dataEmissao;
    private String assinatura;

    public Certificado(){
        super();
    }
    public Certificado(Date dataEmissao, String assinatura){
        this.dataEmissao = dataEmissao;
        this.assinatura = assinatura;
    }

    public Date getDataEmissao(){
        return dataEmissao;
    }
    public String getAssinatura(){
        return assinatura;
    }

    public void setDataEmissao(Date dataEmissao){
        this.dataEmissao = dataEmissao;
    }
    public void setAssinatura(String assinatura){
        this.assinatura = assinatura;
    }
    
    @Override
    public String toString() {
        return dataEmissao + "\n" + assinatura;
    }
}
