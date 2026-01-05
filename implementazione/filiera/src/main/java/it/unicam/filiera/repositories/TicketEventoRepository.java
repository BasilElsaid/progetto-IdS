package it.unicam.filiera.repositories;

import it.unicam.filiera.evento.TicketEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketEventoRepository extends JpaRepository<TicketEvento, Long> {
}
