package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreatePacchettoRequest;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.response.PacchettoResponse;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.PacchettoRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PacchettiService {

    private final PacchettoRepository pacchettoRepo;
    private final ProdottoRepository prodottoRepo;

    public PacchettiService(PacchettoRepository pacchettoRepo, ProdottoRepository prodottoRepo) {
        this.pacchettoRepo = pacchettoRepo;
        this.prodottoRepo = prodottoRepo;
    }

    public List<Pacchetto> lista() {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            return pacchettoRepo.findAll();
        }

        if (isDistributore(u)) {
            return pacchettoRepo.findByDistributoreId(u.getId());
        }

        throw new ForbiddenException("Accesso non consentito");
    }

    public Pacchetto get(Long id) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            return pacchettoRepo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));
        }

        if (isDistributore(u)) {
            return pacchettoRepo
                    .findByIdAndDistributoreId(id, u.getId())
                    .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));
        }

        throw new ForbiddenException("Accesso non consentito");
    }

    public PacchettoResponse crea(CreatePacchettoRequest req) {
        UtenteGenerico u = getUtenteLoggato();

        if (!isDistributore(u) && !isGestorePiattaforma(u)) {
            throw new ForbiddenException("Solo distributori o gestore possono creare pacchetti");
        }

        List<Prodotto> prodotti = new ArrayList<>();
        if (req.prodottiIds() != null && !req.prodottiIds().isEmpty()) {
            for (Long pid : req.prodottiIds()) {
                prodotti.add(prodottoRepo.findById(pid)
                        .orElseThrow(() -> new BadRequestException("Prodotto con id " + pid + " non trovato")));
            }
        }

        // Controllo obbligo almeno 1 prodotto
        if (prodotti.isEmpty()) {
            throw new BadRequestException("Un pacchetto deve contenere almeno un prodotto");
        }

        if (isDistributore(u)) {
            boolean exists = pacchettoRepo.existsByNomeAndDistributoreId(
                    req.nome(),
                    u.getId()
            );

            if (exists) {
                throw new BadRequestException(
                        "Esiste già un pacchetto con questo nome"
                );
            }
        }

        Pacchetto p = new Pacchetto();
        p.setNome(req.nome());
        p.setProdotti(prodotti);

        if (isDistributore(u)) {
            p.setDistributore((DistributoreTipicita) u);
        }

        Pacchetto saved = pacchettoRepo.save(p);
        return PacchettoResponse.from(saved);    }

    public void elimina(Long id) {
        UtenteGenerico u = getUtenteLoggato();

        Pacchetto p = pacchettoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));

        if (isDistributore(u) && !p.getDistributore().getId().equals(u.getId())) {
            throw new ForbiddenException("Pacchetto non tuo");
        }

        pacchettoRepo.delete(p);
    }

    // ================= HELPERS =================
    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico u)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return u;
    }

    private boolean isGestorePiattaforma(UtenteGenerico u) {
        return u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA;
    }

    private boolean isDistributore(UtenteGenerico u) {
        return u instanceof DistributoreTipicita;
    }
}
