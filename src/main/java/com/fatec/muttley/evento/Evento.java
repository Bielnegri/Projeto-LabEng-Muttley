package com.fatec.muttley.evento;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.evento.enums.ModalidadeEventoEnum;
import com.fatec.muttley.evento.enums.StatusEventoEnum;
import com.fatec.muttley.local.Local;
import com.fatec.muttley.participacao.Participacao;
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
@Builder
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private long id;
    private String tema;
    private Date data;
    private String horarioInicio;
    private String horarioFim;

    @Enumerated(EnumType.STRING)
    private ModalidadeEventoEnum modalidade;

    @Enumerated(EnumType.STRING)
    private StatusEventoEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    @JsonManagedReference
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patrocinador")
    private Patrocinador patrocinador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_local")
    private Local local;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;
    private String descricao;

    @OneToMany(mappedBy = "evento")
    @JsonManagedReference
    private List<Participacao> participacoes = new ArrayList<>();

    public Evento(AtualizacaoEvento dados, Disciplina disciplina, Patrocinador patrocinador, Local local) {
        this.tema = dados.tema();
        this.descricao = dados.descricao();
        this.data = dados.data();
        this.horarioInicio = dados.horarioInicio();
        this.horarioFim = dados.horarioFim();
        this.modalidade = dados.modalidade();
        this.disciplina = disciplina;
        this.patrocinador = patrocinador;
        this.local = local;
    }

    public void atualizarInformacoes(AtualizacaoEvento dados, Disciplina disciplina, Patrocinador patrocinador, Local local) {
        if (dados.tema() != null)
            this.tema = dados.tema();
        if (dados.descricao() != null)
            this.descricao = dados.descricao();
        if (dados.data() != null)
            this.data = dados.data();
        if (dados.horarioInicio() != null)
            this.horarioInicio = dados.horarioInicio();
        if (dados.horarioFim() != null)
            this.horarioFim = dados.horarioFim();
        if (dados.modalidade() != null)
            this.modalidade = dados.modalidade();
        if (disciplina != null)
            this.disciplina = disciplina;
        if (patrocinador != null)
            this.patrocinador = patrocinador;
        if (local != null)
            this.local = local;
    }

    public void adicionarParticipacao(Participacao participacao) {
        this.participacoes.add(participacao);
        participacao.setEvento(this);
    }
}
