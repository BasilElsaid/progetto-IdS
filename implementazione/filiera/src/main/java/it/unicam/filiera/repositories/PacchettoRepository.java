package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Pacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {

    List<Pacchetto> findByDistributoreId(Long distributoreId);
    Optional<Pacchetto> findByIdAndDistributoreId(Long id, Long distributoreId);
    boolean existsByNomeAndDistributoreId(String nome, Long distributoreId);
}
