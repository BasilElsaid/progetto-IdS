package it.unicam.filiera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unicam.filiera.models.Acquirente;

import java.util.Optional;

@Repository
public interface AcquirenteRepository extends JpaRepository<Acquirente, Long> {

    Optional<Acquirente> findByUsername(String username);
}
