package it.unicam.filiera.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import it.unicam.filiera.domain.PagamentoOrdine;

public interface PagamentoOrdineRepository extends JpaRepository<PagamentoOrdine, Long> {
    Optional<PagamentoOrdine> findByOrdineId(Long ordineId);
}