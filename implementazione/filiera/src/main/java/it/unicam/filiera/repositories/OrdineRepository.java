package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Ordine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdineRepository extends CrudRepository<Ordine, Long> {
    List<Ordine> findByAcquirenteId(Long acquirenteId);

    List<Ordine> findDistinctByProdotti_IdOrderByDataOraAsc(Long prodottoId);
}
