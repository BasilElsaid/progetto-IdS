package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.TicketEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketEventoRepository extends JpaRepository<TicketEvento, Long> {

    boolean existsByNumeroTicket(int numeroTicket);

    Optional<TicketEvento> findByNumeroTicket(int numeroTicket);

    List<TicketEvento> findByAcquirenteId(Long acquirenteId);

    List<TicketEvento> findByEventoId(Long eventoId);
}
