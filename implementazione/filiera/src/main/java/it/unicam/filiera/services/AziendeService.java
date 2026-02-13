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
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.repositories.UtentiRepository;
import it.unicam.filiera.utilities.CoordinateOSM;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AziendeService {

    private final UtentiRepository utentiRepo;
    private final PasswordEncoder passwordEncoder;

    public AziendeService(UtentiRepository utentiRepo, PasswordEncoder passwordEncoder) {
        this.utentiRepo = utentiRepo;
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
            }
            default -> throw new BadRequestException("Ruolo non gestito dal sistema");
        }

        a = utentiRepo.save(a);

        return UtenteResponse.from(a);
    }

    public List<UtenteResponse> listaAziende() {
        return utentiRepo.findAll().stream()
                .filter(u -> u instanceof Azienda)
                .map(UtenteResponse::from)
                .toList();
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
        utentiRepo.delete(azienda);
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
        return utentiRepo.findById(id)
                .filter(u -> u instanceof Azienda)
                .map(u -> (Azienda) u)
                .orElseThrow(() -> new NotFoundException("Azienda non trovata"));
    }

    private Azienda saveAzienda(Azienda a) {
        return utentiRepo.save(a);
    }

}