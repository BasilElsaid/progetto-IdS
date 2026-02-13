package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdiniRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByAcquirenteId(Long acquirenteId);
}