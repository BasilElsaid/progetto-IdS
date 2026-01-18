package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoStatoRequest;
import it.unicam.filiera.domain.OffertaPacchetto;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.repositories.DistributoreTipicitaRepository;
import it.unicam.filiera.repositories.OffertaPacchettoRepository;
import it.unicam.filiera.repositories.PacchettoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistribuzioneService {

    private final DistributoreTipicitaRepository distributoreRepo;
    private final PacchettoRepository pacchettoRepo;
    private final OffertaPacchettoRepository offertaRepo;

    public DistribuzioneService(DistributoreTipicitaRepository distributoreRepo,
                                PacchettoRepository pacchettoRepo,
                                OffertaPacchettoRepository offertaRepo) {
        this.distributoreRepo = distributoreRepo;
        this.pacchettoRepo = pacchettoRepo;
        this.offertaRepo = offertaRepo;
    }

    public OffertaPacchettoResponse creaOfferta(Long distributoreId, CreateOffertaPacchettoRequest req) {
        DistributoreTipicita d = distributoreRepo.findById(distributoreId).orElseThrow();
        Pacchetto p = pacchettoRepo.findById(req.getPacchettoId()).orElseThrow();

        OffertaPacchetto offerta = new OffertaPacchetto(d, p, req.getPrezzoVendita(), req.getDisponibilita(), req.isAttiva());
        offertaRepo.save(offerta);

        return OffertaPacchettoResponse.from(offerta);
    }

    public List<OffertaPacchettoResponse> listaOfferte(Long distributoreId) {
        return offertaRepo.findByDistributoreId(distributoreId)
                .stream()
                .map(OffertaPacchettoResponse::from)
                .collect(Collectors.toList());
    }

    public OffertaPacchettoResponse aggiornaStatoStock(Long offertaId, UpdateOffertaPacchettoStatoRequest req) {
        OffertaPacchetto offerta = offertaRepo.findById(offertaId).orElseThrow();

        if (req.getAttiva() != null) {
            offerta.setAttiva(req.getAttiva());
        }
        if (req.getDisponibilita() != null) {
            offerta.setDisponibilita(req.getDisponibilita());
        }

        offertaRepo.save(offerta);
        return OffertaPacchettoResponse.from(offerta);
    }
}
