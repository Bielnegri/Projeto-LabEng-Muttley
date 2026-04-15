package com.fatec.muttley.certificado;

import java.sql.Date;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.evento.Evento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certificado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificado")
    private Long id;

    @Column(name = "data_emissao")
    private Date dataEmissao;

    private String assinatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", referencedColumnName = "id_aluno")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", referencedColumnName = "id_evento")
    private Evento evento;

    public Certificado(AtualizacaoCertificado dados, Aluno aluno, Evento evento){
        this.dataEmissao = dados.dataEmissao();
        this.assinatura = dados.assinatura();
        this.aluno = aluno;
        this.evento = evento;
    }

    public void atualizarInformacoes(AtualizacaoCertificado dados, Aluno aluno, Evento evento) {
        if (dados.dataEmissao() != null)
            this.dataEmissao = dados.dataEmissao();
        if (dados.assinatura() != null)
            this.assinatura = dados.assinatura();
        if (aluno != null)
            this.aluno = aluno;
        if (evento != null)
            this.evento = evento;
    }
}