package it.unicam.filiera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.filiera.models.Acquirente;

@Repository
public interface AcquirentiRepository extends JpaRepository<Acquirente, Long> {
}
