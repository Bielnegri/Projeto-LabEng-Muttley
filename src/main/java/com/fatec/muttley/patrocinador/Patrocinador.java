package com.fatec.muttley.patrocinador;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patrocinador")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Patrocinador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patrocinador")
    private Long id;
    private String nome;
    private String cnpj;
    private double valorPatrocinio;
    private String email;
    private String telefone;
    private String site;

    public Patrocinador(AtualizacaoPatrocinador dados){
        this.nome = dados.nome();
        this.cnpj = dados.cnpj();
        this.valorPatrocinio = dados.valorPatrocinio();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.site = dados.site();
    }

    public void atualizarInformacoes(AtualizacaoPatrocinador dados) {
        if (dados.nome() != null)
            this.nome =dados.nome();
        if (dados.cnpj() != null)
            this.cnpj =dados.cnpj();
        if (dados.valorPatrocinio() != 0)
            this.valorPatrocinio = dados.valorPatrocinio();
        if (dados.email() != null)
            this.email =dados.email();
        if (dados.telefone() != null)
            this.telefone =dados.telefone();
        if (dados.site() != null)
            this.site =dados.site();
    }
}
