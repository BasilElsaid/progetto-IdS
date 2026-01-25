package it.unicam.filiera.services;

import it.unicam.filiera.builder.DistributoreTipicitaBuilder;
import it.unicam.filiera.builder.ProduttoreBuilder;
import it.unicam.filiera.builder.TrasformatoreBuilder;
import it.unicam.filiera.dto.create.CreateCoordinateRequest;
import it.unicam.filiera.dto.create.CreateAziendaRequest;
import it.unicam.filiera.dto.update.UpdateAziendaRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.repositories.DistributoreTipicitaRepository;
import it.unicam.filiera.repositories.ProduttoreRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import it.unicam.filiera.utilities.CoordinateOSM;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AziendeService {

    private final ProduttoreRepository produttoreRepo;
    private final TrasformatoreRepository trasformatoreRepo;
    private final DistributoreTipicitaRepository distributoreRepo;
    private final PasswordEncoder passwordEncoder;

    public AziendeService(
            ProduttoreRepository produttoreRepo,
            TrasformatoreRepository trasformatoreRepo,
            DistributoreTipicitaRepository distributoreRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.produttoreRepo = produttoreRepo;
        this.trasformatoreRepo = trasformatoreRepo;
        this.distributoreRepo = distributoreRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteResponse creaAzienda(CreateAziendaRequest request) {
        if (request.nomeAzienda() == null || request.nomeAzienda().isBlank()) {
            throw new BadRequestException("Nome azienda obbligatorio");
        }
        if (request.sede() == null || request.sede().isBlank()) {
            throw new BadRequestException("Sede obbligatoria");
        }
        if (request.coordinate() == null) {
            throw new BadRequestException("Coordinate obbligatorie");
        }

        Ruolo ruolo = request.ruolo();
        Azienda a;
        switch (ruolo) {
            case PRODUTTORE -> {
                a = new ProduttoreBuilder()
                        .setUsername(request.username())
                        .setPassword(passwordEncoder.encode(request.password()))
                        .setEmail(request.email())
                        .setNomeAzienda(request.nomeAzienda())
                        .setPartitaIva(request.partitaIva())
                        .setSede(request.sede())
                        .setCoordinate(toCoordinate(request.coordinate()))
                        .build();
                a = produttoreRepo.save((Produttore) a);
            }
            case TRASFORMATORE -> {
                a = new TrasformatoreBuilder()
                        .setUsername(request.username())
                        .setPassword(passwordEncoder.encode(request.password()))
                        .setEmail(request.email())
                        .setNomeAzienda(request.nomeAzienda())
                        .setPartitaIva(request.partitaIva())
                        .setLaboratorio(request.laboratorio())
                        .setSede(request.sede())
                        .setCoordinate(toCoordinate(request.coordinate()))
                        .build();
                a = trasformatoreRepo.save((Trasformatore) a);
            }
            case DISTRIBUTORE_TIPICITA -> {
                a = new DistributoreTipicitaBuilder()
                        .setUsername(request.username())
                        .setPassword(passwordEncoder.encode(request.password()))
                        .setEmail(request.email())
                        .setNomeAzienda(request.nomeAzienda())
                        .setPartitaIva(request.partitaIva())
                        .setAreaDistribuzione(request.areaDistribuzione())
                        .setSede(request.sede())
                        .setCoordinate(toCoordinate(request.coordinate()))
                        .build();
                a = distributoreRepo.save((DistributoreTipicita) a);
            }
            default -> throw new BadRequestException("Ruolo non gestito dal sistema");
        }

        return UtenteResponse.from(a);
    }

    public List<UtenteResponse> listaAziende() {
        List<UtenteResponse> out = new ArrayList<>();
        produttoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        trasformatoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        distributoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        return out;
    }

    public UtenteResponse getAzienda(Long id) {
        return UtenteResponse.from(findAziendaById(id));
    }

    public UtenteResponse patchAzienda(Long id, UpdateAziendaRequest request) {
        Azienda azienda = findAziendaById(id);

        updateBaseFields(azienda, request);

        if (azienda instanceof Trasformatore t && request.laboratorio() != null) {
            t.setLaboratorio(request.laboratorio());
        }

        if (azienda instanceof DistributoreTipicita d && request.areaDistribuzione() != null) {
            d.setAreaDistribuzione(request.areaDistribuzione());
        }

        return UtenteResponse.from(saveAzienda(azienda));
    }

    public void deleteAzienda(Long id) {
        Azienda azienda = findAziendaById(id);
        deleteAziendaByTipo(azienda);
    }

    // HELPERS
    private CoordinateOSM toCoordinate(CreateCoordinateRequest dto) {
        return dto == null ? null : new CoordinateOSM(dto.lat(), dto.lon());
    }

    private void updateBaseFields(Azienda a, UpdateAziendaRequest request) {
        if (request.email() != null) a.setEmail(request.email());
        if (request.password() != null) a.setPassword(passwordEncoder.encode(request.password()));
        if (request.nomeAzienda() != null) a.setNomeAzienda(request.nomeAzienda());
        if (request.partitaIva() != null) a.setPartitaIva(request.partitaIva());
        if (request.sede() != null) a.setSede(request.sede());
        if (request.coordinate() != null) a.setCoordinate(toCoordinate(request.coordinate()));
    }

    private Azienda findAziendaById(Long id) {
        return produttoreRepo.findById(id).map(a -> (Azienda) a)
                .or(() -> trasformatoreRepo.findById(id).map(a -> (Azienda) a))
                .or(() -> distributoreRepo.findById(id).map(a -> (Azienda) a))
                .orElseThrow(() -> new NotFoundException("Azienda non trovata"));
    }

    private Azienda saveAzienda(Azienda a) {
        if (a instanceof Produttore p) return produttoreRepo.save(p);
        if (a instanceof Trasformatore t) return trasformatoreRepo.save(t);
        if (a instanceof DistributoreTipicita d) return distributoreRepo.save(d);
        throw new IllegalStateException("Tipo di azienda non supportato");
    }

    private void deleteAziendaByTipo(Azienda a) {
        if (a instanceof Produttore p) produttoreRepo.delete(p);
        else if (a instanceof Trasformatore t) trasformatoreRepo.delete(t);
        else if (a instanceof DistributoreTipicita d) distributoreRepo.delete(d);
    }
}