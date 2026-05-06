package com.fatec.muttley.participacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {}