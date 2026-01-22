package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Recensione;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

    boolean existsByOrdineIdAndItemIdAndAcquirenteId(Long ordineId, Long itemId, Long acquirenteId);

    List<Recensione> findByProdottoId(Long prodottoId);  // opzionale, rimane per prodotti
    List<Recensione> findByAcquirenteId(Long acquirenteId);
    List<Recensione> findByItemId(Long itemId);
}