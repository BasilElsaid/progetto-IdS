package it.unicam.filiera.repositories;

import it.unicam.filiera.ordine.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    List<Ordine> findByAcquirenteId(Long acquirenteId);
}
