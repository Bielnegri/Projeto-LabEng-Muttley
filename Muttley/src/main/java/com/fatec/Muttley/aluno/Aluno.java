package com.fatec.Muttley.aluno;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Aluno {
	@Id
	private long id;
	private String nome;
	private String email;
	private String telefone;
	private String instituicao;
	private String matricula;

	public Aluno(){
		super();
	}
	public Aluno(String nome, String email, String telefone, String instituicao, String matricula){
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
		this.instituicao = instituicao;
		this.matricula = matricula;
	}

	public long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getEmail() {
		return email;
	}
	public String getTelefone() {
		return telefone;
	}
	public String getInstituicao() {
		return instituicao;
	}
	public String getMatricula() {
		return matricula;
	}

	public void setId(long id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@Override
	public String toString() {
		return nome + "\n" + email + "\n" + telefone;
	}
}
