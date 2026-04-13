package com.fatec.muttley.medalha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MedalhaRepository extends JpaRepository<Medalha, Long> {
}
