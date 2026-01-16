package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventiRepository extends JpaRepository<Evento, Long> {
}
