package it.unicam.filiera.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.unicam.filiera.domain.OffertaPacchetto;

public interface OffertaPacchettoRepository extends JpaRepository<OffertaPacchetto, Long> {
    List<OffertaPacchetto> findByDistributoreId(Long distributoreId);
}