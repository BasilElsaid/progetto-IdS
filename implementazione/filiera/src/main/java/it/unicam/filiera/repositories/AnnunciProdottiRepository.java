package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.AnnuncioProdotto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.Azienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface AnnunciProdottiRepository extends JpaRepository<AnnuncioProdotto, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AnnuncioProdotto> findById(Long id);

    Optional<AnnuncioProdotto> findByAziendaAndProdotto(Azienda azienda, Prodotto prodotto);

    boolean existsByProdotto_Id(Long prodottoId);

    List<AnnuncioProdotto> findByAzienda_Id(Long aziendaId);

}
