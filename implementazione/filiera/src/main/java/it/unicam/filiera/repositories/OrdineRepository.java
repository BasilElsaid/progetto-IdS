package it.unicam.filiera.repositories;

import it.unicam.filiera.ordine.Ordine;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdineRepository extends CrudRepository<Ordine, Long> {
    List<Ordine> findByAcquirenteId(Long acquirenteId);
}
