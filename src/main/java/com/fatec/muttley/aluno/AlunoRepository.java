package com.fatec.muttley.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AlunoRepository extends JpaRepository<Aluno, Long>{
}