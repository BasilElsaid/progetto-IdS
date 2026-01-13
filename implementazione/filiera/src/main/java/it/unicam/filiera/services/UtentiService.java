package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UtentiService {

    private final AcquirenteRepository acquirenteRepository;
    private final ProduttoreRepository produttoreRepository;
    private final TrasformatoreRepository trasformatoreRepository;
    private final CuratoreRepository curatoreRepository;
    private final AnimatoreRepository animatoreRepository;
    private final DistributoreTipicitaRepository distributoreRepository;
    private final GestorePiattaformaRepository gestoreRepository;

    public UtentiService(
            AcquirenteRepository acquirenteRepository,
            ProduttoreRepository produttoreRepository,
            TrasformatoreRepository trasformatoreRepository,
            CuratoreRepository curatoreRepository,
            AnimatoreRepository animatoreRepository,
            DistributoreTipicitaRepository distributoreRepository,
            GestorePiattaformaRepository gestoreRepository
    ) {
        this.acquirenteRepository = acquirenteRepository;
        this.produttoreRepository = produttoreRepository;
        this.trasformatoreRepository = trasformatoreRepository;
        this.curatoreRepository = curatoreRepository;
        this.animatoreRepository = animatoreRepository;
        this.distributoreRepository = distributoreRepository;
        this.gestoreRepository = gestoreRepository;
    }

    // Endpoint DEMO per il prof: mostra chiaramente tutti i ruoli (guest escluso perché non è persistito)
    public List<UtenteResponse> listaTutti() {
        List<UtenteResponse> out = new ArrayList<>();
        acquirenteRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        produttoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        trasformatoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        curatoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        animatoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        distributoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        gestoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        return out;
    }
}
