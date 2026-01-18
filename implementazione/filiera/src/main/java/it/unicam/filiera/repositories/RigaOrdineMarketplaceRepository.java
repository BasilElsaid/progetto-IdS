package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.RigaOrdineMarketplace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RigaOrdineMarketplaceRepository extends JpaRepository<RigaOrdineMarketplace, Long> {
    List<RigaOrdineMarketplace> findByOrdineId(Long ordineId);
}
