package com.fatec.muttley.participante;

import com.fatec.muttley.aluno.Aluno;
import com.fatec.muttley.evento.Evento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participante")
    private long id;
    private int inscricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    public Participante(int inscricao) {
        this.inscricao = inscricao;
    }
}
