package com.fatec.muttley.palestrante;

import com.fatec.muttley.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "palestrante")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Palestrante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_palestrante")
    private Long id;
    private String resumoProfissional;
    private String empresaAtual;
    private String cargo;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    public Palestrante(AtualizacaoPalestrante dados){
        this.resumoProfissional = dados.resumoProfissional();
        this.empresaAtual = dados.empresaAtual();
        this.cargo = dados.cargo();
    }

    public void atualizarInformacoes(AtualizacaoPalestrante dados) {
        if (dados.resumoProfissional() != null)
            this.resumoProfissional =dados.resumoProfissional();
        if (dados.empresaAtual() != null)
            this.empresaAtual =dados.empresaAtual();
        if (dados.cargo() != null)
            this.cargo =dados.cargo();
    }
}
