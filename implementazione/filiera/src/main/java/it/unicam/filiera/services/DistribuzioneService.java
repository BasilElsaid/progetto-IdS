package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoRequest;
import it.unicam.filiera.domain.OffertaPacchetto;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.repositories.DistributoreRepository;
import it.unicam.filiera.repositories.OffertaPacchettoRepository;
import it.unicam.filiera.repositories.PacchettoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public OffertaPacchettoResponse creaOfferta(Long distributoreId, CreateOffertaPacchettoRequest req) {
        DistributoreTipicita d = distributoreRepo.findById(distributoreId).orElseThrow();
        Pacchetto p = pacchettoRepo.findById(req.getPacchettoId()).orElseThrow();

        OffertaPacchetto offerta = new OffertaPacchetto(
                d,
                p,
                req.getPrezzoVendita(),
                req.getDisponibilita()
        );
        offerta.setAttiva(req.isAttiva());

        return OffertaPacchettoResponse.from(offertaRepo.save(offerta));
    }

    public List<OffertaPacchettoResponse> listaOfferte(Long distributoreId) {
        return offertaRepo.findByDistributoreId(distributoreId).stream().map(OffertaPacchettoResponse::from).toList();
    }

    public OffertaPacchettoResponse aggiornaStatoStock(Long offertaId, UpdateOffertaPacchettoRequest req) {
        OffertaPacchetto offerta = offertaRepo.findById(offertaId).orElseThrow();

        if (req.getAttiva() != null) offerta.setAttiva(req.getAttiva());
        if (req.getDisponibilita() != null) offerta.setDisponibilita(req.getDisponibilita());

        return OffertaPacchettoResponse.from(offertaRepo.save(offerta));
    }
}
