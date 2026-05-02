package com.fatec.muttley.organizador;

import com.fatec.muttley.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organizador")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Organizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organizador")
    private Long id;
    private String instituicao;
    private String cargo;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    public Organizador(AtualizacaoOrganizador dados){
        this.instituicao = dados.instituicao();
        this.cargo = dados.cargo();
    }

    public void atualizarInformacoes(AtualizacaoOrganizador dados) {
        if (dados.instituicao() != null)
            this.instituicao =dados.instituicao();
        if (dados.cargo() != null)
            this.cargo =dados.cargo();
    }
}
