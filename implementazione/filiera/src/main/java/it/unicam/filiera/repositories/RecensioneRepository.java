package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {
    boolean existsByOrdineIdAndProdottoIdAndAcquirenteId(Long ordineId, Long prodottoId, Long acquirenteId);
    List<Recensione> findByProdottoId(Long prodottoId);
    List<Recensione> findByAcquirenteId(Long acquirenteId);
}
