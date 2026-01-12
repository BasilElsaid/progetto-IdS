package it.unicam.filiera.repositories;

import it.unicam.filiera.certificati.Certificato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatoRepository extends JpaRepository<Certificato, Long> {

}