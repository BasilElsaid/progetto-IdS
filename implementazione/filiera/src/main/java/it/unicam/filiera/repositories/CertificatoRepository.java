package it.unicam.filiera.repositories;

import it.unicam.filiera.certificati.Certificato;
import it.unicam.filiera.enums.TipoCertificatore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificatoRepository extends JpaRepository<Certificato, Long> {

    List<Certificato> findByProdottoIdAndTipo(Long prodottoId, TipoCertificatore tipo);

}