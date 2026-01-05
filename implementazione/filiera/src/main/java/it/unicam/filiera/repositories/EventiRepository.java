package it.unicam.filiera.repositories;

import it.unicam.filiera.evento.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventiRepository extends JpaRepository<Evento, Long> {
}
