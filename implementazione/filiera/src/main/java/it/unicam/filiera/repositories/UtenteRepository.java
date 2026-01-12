package it.unicam.filiera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import it.unicam.filiera.models.Produttore;

public interface UtenteRepository extends JpaRepository<Produttore, Long> {
}
