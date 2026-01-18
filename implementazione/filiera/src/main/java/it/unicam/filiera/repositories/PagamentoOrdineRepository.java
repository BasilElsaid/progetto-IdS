package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.PagamentoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoOrdineRepository extends JpaRepository<PagamentoOrdine, Long> {
    PagamentoOrdine findByOrdineId(Long ordineId);
}
