package it.unicam.filiera.repositories;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.Produttore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    List<Prodotto> findByProduttore(Produttore produttore);

    Optional<Prodotto> findByIdAndProduttore(Long id, Produttore produttore);

    /*
	List<Prodotto> findByProduttore_Id(Long id);

    List<Prodotto> findByNomeContainingIgnoreCase(String nome);

    List<Prodotto> findByOrigineContainingIgnoreCase(String origine);

    List<Prodotto> findByNomeContainingIgnoreCaseAndOrigineContainingIgnoreCase(
        String nome, String origine
    );
    
     */
}

