package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Recensione;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

    List<Recensione> findByAcquirenteId(Long acquirenteId);
    List<Recensione> findByOrdineId(Long ordineId);
}