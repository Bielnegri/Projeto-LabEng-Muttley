package com.fatec.muttley.professor;

import com.fatec.muttley.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "professor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Professor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_professor")
    private long id;
    private String areaFormacao;
    private String titulacao;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    public Professor(AtualizacaoProfessor dados){
        this.areaFormacao = dados.areaFormacao();
        this.titulacao = dados.titulacao();
    }

    public void AtualizarInformações(AtualizacaoProfessor dados){
        if (dados.areaFormacao() != null)
            this.areaFormacao = dados.areaFormacao();
        if(dados.titulacao() != null)
            this.titulacao = dados.titulacao();
    }
}
