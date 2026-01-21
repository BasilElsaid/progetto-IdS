package it.unicam.filiera.repositories;

import it.unicam.filiera.certificati.CertificatoCuratore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatoCuratoreRepository extends JpaRepository<CertificatoCuratore, Long> {
    boolean existsByCertificatoTarget_IdAndApprovatoTrue(Long targetId);
}