package it.unicam.filiera.repositories;

import it.unicam.filiera.prodotto.Pacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacchettiRepo extends JpaRepository<Pacchetto, Long> { }
