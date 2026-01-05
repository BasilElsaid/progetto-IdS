package it.unicam.filiera.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.TipoAzienda;

public interface AziendaRepository extends JpaRepository<Azienda, Long> {

    List<Azienda> findByTipo(TipoAzienda tipo);
}
