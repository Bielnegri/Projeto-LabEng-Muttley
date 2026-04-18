package com.fatec.muttley.evento;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fatec.muttley.participante.Participante;
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

    @OneToMany(mappedBy = "evento")
    private List<Participante> participantes = new ArrayList<>();

    public Evento(String tema, String local, Date data, String horario) {
        this.tema = tema;
        this.local = local;
        this.data = data;
        this.horario = horario;
    }
}
