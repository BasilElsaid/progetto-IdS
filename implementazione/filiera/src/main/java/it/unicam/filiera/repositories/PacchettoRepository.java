package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Pacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {

    List<Pacchetto> findByDistributoreId(Long distributoreId);
    boolean existsByIdAndDistributoreId(Long id, Long distributoreId);
    Pacchetto findByIdAndDistributoreId(Long id, Long distributoreId);
}
