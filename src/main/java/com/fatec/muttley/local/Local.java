package com.fatec.muttley.local;

import com.fatec.muttley.endereco.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "local")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private Long id;
    private String nome;
    private String descricao;
    private int capacidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    public Local(AtualizacaoLocal dados, Endereco endereco){
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.capacidade = dados.capacidade();
        this.endereco = endereco;
    }

    public void atualizarInformacoes(AtualizacaoLocal dados, Endereco endereco) {
        if (dados.nome() != null)
            this.nome =dados.nome();
        if (dados.descricao() != null)
            this.descricao =dados.descricao();
        if (dados.capacidade() != 0)
            this.capacidade = dados.capacidade();
        if (endereco != null)
            this.endereco = endereco;
    }
}
