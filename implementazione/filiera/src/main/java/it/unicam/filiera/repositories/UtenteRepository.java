package it.unicam.filiera.repositories;

import it.unicam.filiera.models.UtenteGenerico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<UtenteGenerico, Long> { }
