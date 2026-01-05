package it.unicam.filiera.services;

import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.ProduttoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtentiService {

    private final ProduttoreRepository produttoreRepository;

    public UtentiService(ProduttoreRepository produttoreRepository) {
        this.produttoreRepository = produttoreRepository;
    }

    @Transactional
    public Produttore creaProduttore(Produttore p) {
        // vincoli “reali”
        if (produttoreRepository.findByUsername(p.getUsername()).isPresent())
            throw new BadRequestException("Username già in uso.");
        if (produttoreRepository.findByEmail(p.getEmail()).isPresent())
            throw new BadRequestException("Email già in uso.");
        if (produttoreRepository.findByPartitaIva(p.getPartitaIva()).isPresent())
            throw new BadRequestException("Partita IVA già registrata.");

        p.setRuolo(Ruolo.PRODUTTORE);
        return produttoreRepository.save(p);
    }

    @Transactional(readOnly = true)
    public Produttore getProduttore(Long id) {
        return produttoreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produttore non trovato: id=" + id));
    }
}
