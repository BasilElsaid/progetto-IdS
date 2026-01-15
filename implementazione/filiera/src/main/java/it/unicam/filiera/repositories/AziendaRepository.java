package it.unicam.filiera.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.filiera.models.Azienda;

public interface AziendaRepository extends JpaRepository<Azienda, Long> {

}
