package com.fatec.muttley.disciplina;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

}
