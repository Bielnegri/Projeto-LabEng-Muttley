package com.fatec.muttley.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LocalRepository extends JpaRepository<Local, Long> {
}
