package com.fatec.muttley.aluno;

import com.fatec.muttley.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Aluno {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_aluno")
	private Long id;
	private String instituicao;
	private String matricula;

	@OneToOne
	@MapsId
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	public Aluno(AtualizacaoAluno dados){
		this.instituicao = dados.instituicao();
		this.matricula = dados.matricula();
    }

	public void atualizarInformacoes(AtualizacaoAluno dados) {
		if (dados.instituicao() != null)
			this.instituicao =dados.instituicao();
		if (dados.matricula() != null)
			this.matricula =dados.matricula();
	}
}
