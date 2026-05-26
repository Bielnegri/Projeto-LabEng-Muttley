package com.fatec.muttley.patrocinador;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PatrocinadorRepository extends JpaRepository<Patrocinador, Long> {
}
