package com.fatec.muttley.colaborador;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fatec.muttley.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "colaborador")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Colaborador {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Long id;
    private String funcao;
    private String disponibilidade;
    private String tipo;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_pessoa")
    @JsonManagedReference
    private Pessoa pessoa;

    public Colaborador(AtualizacaoColaborador dados){
        this.funcao = dados.funcao();
        this.disponibilidade = dados.disponibilidade();
        this.tipo = dados.tipo();
    }

    public void atualizarInformacoes(AtualizacaoColaborador dados) {
        if (dados.funcao() != null)
            this.funcao =dados.funcao();
        if (dados.disponibilidade() != null)
            this.disponibilidade =dados.disponibilidade();
        if (dados.tipo() != null)
            this.tipo =dados.tipo();
    }
}
