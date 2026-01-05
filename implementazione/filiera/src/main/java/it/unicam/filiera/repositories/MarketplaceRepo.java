package it.unicam.filiera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.unicam.filiera.models.Prodotto;

public interface MarketplaceRepo extends JpaRepository<Prodotto, Long> { }
