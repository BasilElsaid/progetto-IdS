package it.unicam.filiera.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.unicam.filiera.domain.RigaOrdineMarketplace;

public interface RigaOrdineMarketplaceRepository extends JpaRepository<RigaOrdineMarketplace, Long> {
    List<RigaOrdineMarketplace> findByOrdineId(Long ordineId);
}