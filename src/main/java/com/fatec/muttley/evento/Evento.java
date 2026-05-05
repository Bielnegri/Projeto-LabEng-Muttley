package com.fatec.muttley.evento;

import java.sql.Date;

import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.local.Local;
import com.fatec.muttley.patrocinador.Patrocinador;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private long id;
    private String tema;
    private Date data;
    private String horarioInicio;
    private String horarioFim;
    private String modalidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patrocinador")
    private Patrocinador patrocinador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local")
    private Local local;

    public Evento(AtualizacaoEvento dados, Disciplina disciplina, Patrocinador patrocinador, Local local) {
        this.tema = dados.tema();
        this.data = dados.data();
        this.horarioInicio = dados.horarioInicio();
        this.horarioFim = dados.horarioFim();
        this.modalidade = dados.modalidade();
    }

    public void atualizarInformacoes(AtualizacaoEvento dados, Disciplina disciplina, Patrocinador patrocinador, Local local) {
        if (dados.tema() != null)
            this.tema = dados.tema();
        if (dados.data() != null)
            this.data = dados.data();
        if (dados.horarioInicio() != null)
            this.horarioInicio = dados.horarioInicio();
        if (dados.horarioFim() != null)
            this.horarioFim = dados.horarioFim();
        if (dados.modalidade() != null)
            this.modalidade = dados.modalidade();
    }
}
