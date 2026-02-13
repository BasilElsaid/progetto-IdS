package it.unicam.filiera.repositories;

import it.unicam.filiera.certificati.Certificato;
import it.unicam.filiera.enums.TipoCertificatore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatiRepository extends JpaRepository<Certificato, Long> {
    boolean existsByProdottoIdAndTipo(Long prodottoId, TipoCertificatore tipo);
}