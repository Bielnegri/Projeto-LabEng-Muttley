package com.fatec.muttley.participante;

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

    public Participante(int inscricao) {
        this.inscricao = inscricao;
    }
}
