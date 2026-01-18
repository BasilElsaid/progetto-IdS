package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.TrasformazioneProdotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrasformazioneProdottoRepository extends JpaRepository<TrasformazioneProdotto, Long> {
    List<TrasformazioneProdotto> findByProcessoId(Long processoId);
    List<TrasformazioneProdotto> findByTrasformatoreId(Long trasformatoreId);
    List<TrasformazioneProdotto> findByProcessoIdAndTrasformatoreId(
            Long processoId,
            Long trasformatoreId
    );
}
