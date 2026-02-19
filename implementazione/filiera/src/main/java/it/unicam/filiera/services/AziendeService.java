package it.unicam.filiera.services;

import it.unicam.filiera.builder.DistributoreTipicitaBuilder;
import it.unicam.filiera.builder.ProduttoreBuilder;
import it.unicam.filiera.builder.TrasformatoreBuilder;
import it.unicam.filiera.dto.create.CreateAziendaRequest;
import it.unicam.filiera.dto.update.UpdateAziendaRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.UtentiRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AziendeService {

    private final UtentiRepository utentiRepo;
    private final PasswordEncoder passwordEncoder;
    private final PositionService positionService;

    public AziendeService(UtentiRepository utentiRepo, PasswordEncoder passwordEncoder, PositionService positionService) {
        this.utentiRepo = utentiRepo;
        this.passwordEncoder = passwordEncoder;
        this.positionService = positionService;
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
                        .setCoordinate(positionService.validaEGenera(request.coordinate()))
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
                        .setCoordinate(positionService.validaEGenera(request.coordinate()))
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
                        .setCoordinate(positionService.validaEGenera(request.coordinate()))
                        .build();
            }
            default -> throw new BadRequestException("Ruolo non gestito dal sistema");
        }

        a = utentiRepo.save(a);

        return UtenteResponse.from(a);
    }

    public List<UtenteResponse> listaAziende() {
        UtenteGenerico u = getUtenteLoggato();

        if (u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA) {
            // Tutte le aziende
            return utentiRepo.findAll().stream()
                    .filter(a -> a instanceof Azienda)
                    .map(UtenteResponse::from)
                    .toList();
        }

        // Per tutte le altre aziende, restituisci solo la propria
        if (u instanceof Azienda azienda) {
            return List.of(UtenteResponse.from(azienda));
        }

        // Altri utenti non azienda non hanno accesso
        throw new ForbiddenException("Non puoi vedere le aziende");
    }

    public UtenteResponse patchAzienda(Long id, UpdateAziendaRequest request) {
        UtenteGenerico u = getUtenteLoggato();
        Azienda azienda = findAziendaById(id);

        // Controllo proprietà o ruolo gestore
        if (!u.getId().equals(azienda.getId())) {
            throw new ForbiddenException("Non puoi modificare questa azienda");
        }

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
        UtenteGenerico u = getUtenteLoggato();
        Azienda azienda = findAziendaById(id);

        // Controllo proprietà o ruolo gestore
        if (!u.getId().equals(azienda.getId()) && u.getRuolo() != Ruolo.GESTORE_PIATTAFORMA) {
            throw new ForbiddenException("Non puoi eliminare questa azienda");
        }

        utentiRepo.delete(azienda);
    }

    private void updateBaseFields(Azienda a, UpdateAziendaRequest request) {
        if (request.email() != null) a.setEmail(request.email());
        if (request.password() != null) a.setPassword(passwordEncoder.encode(request.password()));
        if (request.nomeAzienda() != null) a.setNomeAzienda(request.nomeAzienda());
        if (request.partitaIva() != null) a.setPartitaIva(request.partitaIva());
        if (request.sede() != null) a.setSede(request.sede());
        if (request.coordinate() != null) a.setCoordinate(positionService.validaEGenera(request.coordinate()));
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

    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return user;
    }

}