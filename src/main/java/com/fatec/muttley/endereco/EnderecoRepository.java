package com.fatec.muttley.endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
