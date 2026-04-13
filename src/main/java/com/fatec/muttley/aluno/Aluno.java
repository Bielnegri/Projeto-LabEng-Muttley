package com.fatec.muttley.aluno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "aluno_id")
	private Long id;
	private String nome;
	private String email;
	private String telefone;
	private String instituicao;
	private String matricula;

	public Aluno(AtualizacaoAluno dados){
        this.nome = dados.nome();
        this.email = dados.email();
		this.telefone = dados.telefone();
		this.instituicao = dados.instituicao();
		this.matricula = dados.matricula();
    }

	public void atualizarInformacoes(AtualizacaoAluno dados) {
		if (dados.nome() != null )
			this.nome = dados.nome();
		if (dados.email() != null)
			this.email =dados.email();
		if (dados.telefone() != null)
			this.telefone =dados.telefone();
		if (dados.instituicao() != null)
			this.instituicao =dados.instituicao();
		if (dados.matricula() != null)
			this.matricula =dados.matricula();
	}
}
