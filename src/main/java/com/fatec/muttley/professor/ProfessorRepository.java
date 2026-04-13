package com.fatec.muttley.professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
