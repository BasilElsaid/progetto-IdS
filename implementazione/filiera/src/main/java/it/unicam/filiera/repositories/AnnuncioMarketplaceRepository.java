package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.enums.CategoriaProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface AnnuncioMarketplaceRepository extends JpaRepository<AnnuncioMarketplace, Long> {

    List<AnnuncioMarketplace> findByAziendaId(Long aziendaId);
    List<AnnuncioMarketplace> findByProdottoCategoria(CategoriaProdotto categoria);
    List<AnnuncioMarketplace> findByAttivo(boolean attivo);
    List<AnnuncioMarketplace> findByAziendaIdAndAttivo(Long aziendaId, boolean attivo);
    List<AnnuncioMarketplace> findByAziendaIdAndProdottoCategoria(Long aziendaId, CategoriaProdotto categoria);
    List<AnnuncioMarketplace> findByAziendaIdAndProdottoCategoriaAndAttivo(Long aziendaId, CategoriaProdotto categoria, boolean attivo);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AnnuncioMarketplace a where a.id = :id")
    Optional<AnnuncioMarketplace> findByIdForUpdate(@Param("id") Long id);
}
