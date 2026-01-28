package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByAcquirenteId(Long acquirenteId);
}