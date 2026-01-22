package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Ordine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdineRepository extends CrudRepository<Ordine, Long> {

    List<Ordine> findByAcquirenteId(Long acquirenteId);

    // Se vuoi filtrare per annuncio (prodotto o pacchetto) ora devi fare join su items
    // Con JPQL / @Query:
    @Query("SELECT DISTINCT o FROM Ordine o JOIN o.items i WHERE i.annuncioId = :annuncioId ORDER BY o.dataCreazione ASC")
    List<Ordine> findByAnnuncioIdOrderByDataCreazioneAsc(Long annuncioId);
}