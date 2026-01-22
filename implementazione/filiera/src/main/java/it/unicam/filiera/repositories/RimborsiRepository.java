package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.RichiestaRimborso;
import it.unicam.filiera.enums.StatoRimborso;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RimborsiRepository extends CrudRepository<RichiestaRimborso, Long> {

    // Tutte le richieste di un dato acquirente
    List<RichiestaRimborso> findByAcquirenteId(Long acquirenteId);

    // Tutte le richieste filtrate per stato
    List<RichiestaRimborso> findByStato(StatoRimborso stato);
}