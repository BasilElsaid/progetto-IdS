package it.unicam.filiera.repositories;

import it.unicam.filiera.models.Produttore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProduttoreRepository extends JpaRepository<Produttore, Long> {
    Optional<Produttore> findByUsername(String username);
    Optional<Produttore> findByEmail(String email);
    Optional<Produttore> findByPartitaIva(String partitaIva);
}
