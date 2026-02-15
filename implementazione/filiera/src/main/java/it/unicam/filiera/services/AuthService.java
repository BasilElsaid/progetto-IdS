package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateAcquirenteRequest;
import it.unicam.filiera.dto.create.CreateAziendaRequest;
import it.unicam.filiera.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.models.*;
import it.unicam.filiera.repositories.UtentiRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtentiRepository utenteRepository;
    private final AziendeService aziendaService;
    private final PersonaleService personaleService;
    private final AcquirentiService acquirenteService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtentiRepository utenteRepository, AziendeService aziendaService, PersonaleService personaleService, AcquirentiService acquirenteService,
                       PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.aziendaService = aziendaService;
        this.personaleService = personaleService;
        this.acquirenteService = acquirenteService;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteGenerico authenticate(String username, String password) {
        UtenteGenerico u = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Utente non trovato"));

        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new BadRequestException("Password errata");
        }

        return u;
    }

    public UtenteResponse registerAzienda(CreateAziendaRequest req) {
        return aziendaService.creaAzienda(req);
    }

    public UtenteResponse registerPersonale(CreatePersonaleRequest req) {
        return personaleService.creaPersonale(req);
    }

    public UtenteResponse registerAcquirente(CreateAcquirenteRequest req) {
        return acquirenteService.creaAcquirente(req);
    }
}