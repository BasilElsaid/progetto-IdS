package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LottoRepository extends JpaRepository<Lotto, Long> {
    Optional<Lotto> findByQrCode(String qrCode);
}
