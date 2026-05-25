package com.fatec.muttley.participacao;

import com.fatec.muttley.evento.Evento;
import com.fatec.muttley.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Participacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacao")
    private long id;
    private int inscricao;
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", nullable = false)
    private Pessoa pessoa;

    public Participacao(AtualizacaoParticipacao dados, Pessoa pessoa, Evento evento){
        this.inscricao = dados.inscricao();
        this.tipo = dados.tipo();
        this.pessoa = pessoa;
        this.evento = evento;
    }

    public void atualizarInformacoes(AtualizacaoParticipacao dados, Pessoa pessoa, Evento evento) {
        if (dados.inscricao() != 0)
            this.inscricao = dados.inscricao();
        if (dados.tipo() != null)
            this.tipo = dados.tipo();
        if (pessoa != null)
            this.pessoa = pessoa;
        if (evento != null)
            this.evento = evento;
    }
}

