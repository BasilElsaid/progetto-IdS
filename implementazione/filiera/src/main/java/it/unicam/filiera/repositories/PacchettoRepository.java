package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Pacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacchettoRepository extends JpaRepository<Pacchetto, Long> {
}
