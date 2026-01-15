package it.unicam.filiera.services;

import it.unicam.filiera.builder.DistributoreTipicitaBuilder;
import it.unicam.filiera.builder.ProduttoreBuilder;
import it.unicam.filiera.builder.TrasformatoreBuilder;
import it.unicam.filiera.controllers.dto.CoordinateDTO;
import it.unicam.filiera.controllers.dto.CreateAziendaRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.*;
import it.unicam.filiera.repositories.DistributoreTipicitaRepository;
import it.unicam.filiera.repositories.ProduttoreRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import it.unicam.filiera.utilities.CoordinateOSM;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AziendeService {

    private final ProduttoreRepository produttoreRepo;
    private final TrasformatoreRepository trasformatoreRepo;
    private final DistributoreTipicitaRepository distributoreRepo;

    public AziendeService(
            ProduttoreRepository produttoreRepo,
            TrasformatoreRepository trasformatoreRepo,
            DistributoreTipicitaRepository distributoreRepo
    ) {
        this.produttoreRepo = produttoreRepo;
        this.trasformatoreRepo = trasformatoreRepo;
        this.distributoreRepo = distributoreRepo;
    }

    // ================= CREATE =================
    public Azienda creaAzienda(CreateAziendaRequest request) {
        Ruolo ruolo = request.getRuolo();
        switch (ruolo) {
            case PRODUTTORE -> {
                Produttore p = (Produttore) new ProduttoreBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                        .setEmail(request.getEmail())
                        .setNomeAzienda(request.getNomeAzienda())
                        .setPartitaIva(request.getPartitaIva())
                        .setCoordinate(toCoordinate(request.getCoordinate()))
                        .build();
                return produttoreRepo.save(p);
            }
            case TRASFORMATORE -> {
                Trasformatore t = (Trasformatore) new TrasformatoreBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                        .setEmail(request.getEmail())
                        .setNomeAzienda(request.getNomeAzienda())
                        .setPartitaIva(request.getPartitaIva())
                        .setLaboratorio(request.getLaboratorio())
                        .setCoordinate(toCoordinate(request.getCoordinate()))
                        .build();
                return trasformatoreRepo.save(t);
            }
            case DISTRIBUTORE_TIPICITA -> {
                DistributoreTipicita d = (DistributoreTipicita) new DistributoreTipicitaBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                        .setEmail(request.getEmail())
                        .setNomeAzienda(request.getNomeAzienda())
                        .setPartitaIva(request.getPartitaIva())
                        .setSede(request.getSede())
                        .setAreaDistribuzione(request.getAreaDistribuzione())
                        .setCoordinate(toCoordinate(request.getCoordinate()))
                        .build();
                return distributoreRepo.save(d);
            }
            default -> throw new BadRequestException("Ruolo non gestito dal sistema");
        }
    }

    // ================= READ =================
    public List<UtenteResponse> listaAziende() {
        List<UtenteResponse> out = new ArrayList<>();
        produttoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        trasformatoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        distributoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        return out;
    }

    public UtenteResponse getAzienda(Long id) {
        return produttoreRepo.findById(id).map(UtenteResponse::from)
                .or(() -> trasformatoreRepo.findById(id).map(UtenteResponse::from))
                .or(() -> distributoreRepo.findById(id).map(UtenteResponse::from))
                .orElseThrow(() -> new NotFoundException("Azienda non trovata"));
    }

    // ================= PATCH =================
    public UtenteResponse patchAzienda(Long id, CreateAziendaRequest request) {
        if (produttoreRepo.existsById(id)) {
            Produttore p = produttoreRepo.findById(id).get();
            updateBaseFields(p, request);
            return UtenteResponse.from(produttoreRepo.save(p));
        }
        if (trasformatoreRepo.existsById(id)) {
            Trasformatore t = trasformatoreRepo.findById(id).get();
            updateBaseFields(t, request);
            if (request.getLaboratorio() != null) t.setLaboratorio(request.getLaboratorio());
            return UtenteResponse.from(trasformatoreRepo.save(t));
        }
        if (distributoreRepo.existsById(id)) {
            DistributoreTipicita d = distributoreRepo.findById(id).get();
            updateBaseFields(d, request);
            if (request.getAreaDistribuzione() != null) d.setAreaDistribuzione(request.getAreaDistribuzione());
            return UtenteResponse.from(distributoreRepo.save(d));
        }
        throw new NotFoundException("Azienda non trovata");
    }

    // ================= DELETE =================
    public void deleteAzienda(Long id) {
        if (produttoreRepo.existsById(id)) {
            produttoreRepo.deleteById(id);
            return;
        }
        if (trasformatoreRepo.existsById(id)) {
            trasformatoreRepo.deleteById(id);
            return;
        }
        if (distributoreRepo.existsById(id)) {
            distributoreRepo.deleteById(id);
            return;
        }
        throw new NotFoundException("Azienda non trovata");
    }

    // ================= HELPERS =================
    private CoordinateOSM toCoordinate(CoordinateDTO dto) {
        return dto == null ? null : new CoordinateOSM(dto.lat, dto.lon);
    }

    private void updateBaseFields(Azienda a, CreateAziendaRequest request) {
        if (request.getEmail() != null) a.setEmail(request.getEmail());
        if (request.getPassword() != null) a.setPassword(request.getPassword());
        if (request.getNomeAzienda() != null) a.setNomeAzienda(request.getNomeAzienda());
        if (request.getPartitaIva() != null) a.setPartitaIva(request.getPartitaIva());
        if (request.getCoordinate() != null) a.setCoordinate(toCoordinate(request.getCoordinate()));
    }
}