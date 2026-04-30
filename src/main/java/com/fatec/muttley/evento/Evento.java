package com.fatec.muttley.evento;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fatec.muttley.disciplina.Disciplina;
import com.fatec.muttley.participacao.Participacao;
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
    private String local;
    private Date data;
    private String horario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    @OneToMany(mappedBy = "evento")
    private List<Participacao> participacaos = new ArrayList<>();

    public Evento(String tema, String local, Date data, String horario) {
        this.tema = tema;
        this.local = local;
        this.data = data;
        this.horario = horario;
    }
}
