package it.unicam.filiera.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unicam.filiera.domain.OffertaPacchetto;
import it.unicam.filiera.repositories.OffertaPacchettoRepository;
import it.unicam.filiera.repositories.DistributoreRepository;
import it.unicam.filiera.repositories.PacchettoRepository;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;

import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.domain.Pacchetto;

@Service
public class DistribuzioneService {

    private final OffertaPacchettoRepository offertaRepo;
    private final DistributoreRepository distributoreRepo;
    private final PacchettoRepository pacchettoRepo;

    public DistribuzioneService(OffertaPacchettoRepository offertaRepo,
                                DistributoreRepository distributoreRepo,
                                PacchettoRepository pacchettoRepo) {
        this.offertaRepo = offertaRepo;
        this.distributoreRepo = distributoreRepo;
        this.pacchettoRepo = pacchettoRepo;
    }

    @Transactional
    public OffertaPacchettoResponse creaOfferta(Long distributoreId, CreateOffertaPacchettoRequest req) {
        DistributoreTipicita d = distributoreRepo.findById(distributoreId).orElseThrow();
        Pacchetto p = pacchettoRepo.findById(req.pacchettoId).orElseThrow();

        OffertaPacchetto o = new OffertaPacchetto();
        o.setDistributore(d);
        o.setPacchetto(p);
        o.setPrezzoVendita(req.prezzoVendita);
        o.setDisponibilita(req.disponibilita);
        o.setAttiva(req.attiva);

        return OffertaPacchettoResponse.from(offertaRepo.save(o));
    }

    @Transactional(readOnly = true)
    public List<OffertaPacchettoResponse> listaOffertePerDistributore(Long distributoreId) {
        return offertaRepo.findByDistributoreId(distributoreId).stream()
                .map(OffertaPacchettoResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OffertaPacchettoResponse aggiornaStatoOfferta(Long offertaId, UpdateOffertaPacchettoRequest req) {
        OffertaPacchetto o = offertaRepo.findById(offertaId).orElseThrow();
        if (req.disponibilita != null) o.setDisponibilita(req.disponibilita);
        if (req.attiva != null) o.setAttiva(req.attiva);
        if (req.prezzoVendita != null) o.setPrezzoVendita(req.prezzoVendita);
        return OffertaPacchettoResponse.from(offertaRepo.save(o));
    }
}