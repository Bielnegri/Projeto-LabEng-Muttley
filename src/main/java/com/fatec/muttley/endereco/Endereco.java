package com.fatec.muttley.endereco;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long id;
    private String estado;
    private String cidade;
    private String bairro;
    private String logradouro;
    private int numero;
    private String complemento;

    public Endereco(AtualizacaoEndereco dados){
        this.estado = dados.estado();
        this.cidade = dados.cidade();
        this.bairro = dados.bairro();
        this.logradouro = dados.logradouro();
        this.numero = dados.numero();
        this.complemento = dados.complemento();
    }

    public void atualizarInformacoes(AtualizacaoEndereco dados) {
        if (dados.estado() != null)
            this.estado =dados.estado();
        if (dados.cidade() != null)
            this.cidade =dados.cidade();
        if (dados.bairro() != null)
            this.bairro = dados.bairro();
        if (dados.logradouro() != null)
            this.logradouro =dados.logradouro();
        if (dados.numero() != 0)
            this.numero =dados.numero();
        if (dados.complemento() != null)
            this.complemento =dados.complemento();
    }
}
