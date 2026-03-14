package com.fatec.Muttley.aluno;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Aluno {
	@Id
	long id;
	String nome;
	String matricula;
}
