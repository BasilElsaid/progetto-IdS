package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.RichiestaRimborso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RichiestaRimborsoRepository extends JpaRepository<RichiestaRimborso, Long> {
    List<RichiestaRimborso> findByAcquirenteId(Long acquirenteId);
    boolean existsByOrdineId(Long ordineId);
}
