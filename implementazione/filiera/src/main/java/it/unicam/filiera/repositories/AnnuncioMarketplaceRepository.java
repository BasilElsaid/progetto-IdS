package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.enums.CategoriaProdotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnuncioMarketplaceRepository extends JpaRepository<AnnuncioMarketplace, Long> {
    List<AnnuncioMarketplace> findByAziendaId(Long aziendaId);
    List<AnnuncioMarketplace> findByProdottoCategoria(CategoriaProdotto categoria);
    List<AnnuncioMarketplace> findByAttivo(boolean attivo);
    List<AnnuncioMarketplace> findByAziendaIdAndAttivo(Long aziendaId, boolean attivo);
    List<AnnuncioMarketplace> findByAziendaIdAndProdottoCategoria(Long aziendaId, CategoriaProdotto categoria);
    List<AnnuncioMarketplace> findByAziendaIdAndProdottoCategoriaAndAttivo(Long aziendaId, CategoriaProdotto categoria, boolean attivo);
}
