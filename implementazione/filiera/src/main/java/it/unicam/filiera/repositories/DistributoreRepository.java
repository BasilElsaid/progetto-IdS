package it.unicam.filiera.repositories;

import it.unicam.filiera.models.DistributoreTipicita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributoreRepository extends JpaRepository<DistributoreTipicita, Long> {
}
