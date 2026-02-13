package it.unicam.filiera.repositories;

import it.unicam.filiera.certificati.CertificatoCuratore;
import it.unicam.filiera.enums.TipoCertificatore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificatiCuratoreRepository extends JpaRepository<CertificatoCuratore, Long> {
    boolean existsByCertificatoTargetId(Long certificatoId);
    boolean existsByCertificatoTargetProdottoIdAndApprovatoTrue(Long prodottoId);
    boolean existsByCertificatoTarget_ProdottoIdAndCertificatoTarget_TipoAndApprovatoTrue(
            Long prodottoId,
            TipoCertificatore tipo
    );
}