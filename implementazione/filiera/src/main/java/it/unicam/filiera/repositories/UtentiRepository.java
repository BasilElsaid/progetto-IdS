package it.unicam.filiera.repositories;

import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.models.UtenteGenerico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtentiRepository extends JpaRepository<UtenteGenerico, Long> {
    Optional<UtenteGenerico> findByUsername(String username);
    boolean existsByRuolo(Ruolo ruolo);
}
