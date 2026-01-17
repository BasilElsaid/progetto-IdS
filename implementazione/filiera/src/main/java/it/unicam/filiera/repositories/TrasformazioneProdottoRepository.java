package it.unicam.filiera.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.unicam.filiera.domain.TrasformazioneProdotto;

public interface TrasformazioneProdottoRepository extends JpaRepository<TrasformazioneProdotto, Long> {
    List<TrasformazioneProdotto> findByProcessoId(Long processoId);
    List<TrasformazioneProdotto> findByTrasformatoreId(Long trasformatoreId);
}