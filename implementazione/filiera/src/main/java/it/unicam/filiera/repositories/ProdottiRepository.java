package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdottiRepository extends JpaRepository<Prodotto, Long> {

    Optional<Prodotto> findByIdAndProprietario(Long id, Azienda proprietario);
    List<Prodotto> findByProprietario(Azienda proprietario);
}

