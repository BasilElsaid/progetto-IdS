package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.repositories.AnimatoreRepository;
import it.unicam.filiera.repositories.CuratoreRepository;
import it.unicam.filiera.repositories.GestorePiattaformaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaleService {

    private final CuratoreRepository curatoreRepository;
    private final AnimatoreRepository animatoreRepository;
    private final GestorePiattaformaRepository gestoreRepository;

    public PersonaleService(
            CuratoreRepository curatoreRepository,
            AnimatoreRepository animatoreRepository,
            GestorePiattaformaRepository gestoreRepository
    ) {
        this.curatoreRepository = curatoreRepository;
        this.animatoreRepository = animatoreRepository;
        this.gestoreRepository = gestoreRepository;
    }

    public List<UtenteResponse> listaPersonale() {
        List<UtenteResponse> out = new ArrayList<>();
        curatoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        animatoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        gestoreRepository.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        return out;
    }
}