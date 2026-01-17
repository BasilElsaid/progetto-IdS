package it.unicam.filiera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import it.unicam.filiera.domain.AnnuncioMarketplace;

public interface AnnuncioMarketplaceRepository extends JpaRepository<AnnuncioMarketplace, Long>, JpaSpecificationExecutor<AnnuncioMarketplace> {
}