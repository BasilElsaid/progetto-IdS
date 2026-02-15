package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.AnnuncioPacchetto;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.models.Azienda;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface AnnunciPacchettiRepository extends JpaRepository<AnnuncioPacchetto, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AnnuncioPacchetto> findById(Long id);

    Optional<AnnuncioPacchetto> findByAziendaAndPacchetto(Azienda azienda, Pacchetto pacchetto);

    boolean existsByPacchetto_Id(Long pacchettoId);
}
