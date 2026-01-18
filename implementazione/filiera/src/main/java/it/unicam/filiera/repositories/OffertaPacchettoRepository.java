package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.OffertaPacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffertaPacchettoRepository extends JpaRepository<OffertaPacchetto, Long> {
    List<OffertaPacchetto> findByDistributoreId(Long distributoreId);
}
