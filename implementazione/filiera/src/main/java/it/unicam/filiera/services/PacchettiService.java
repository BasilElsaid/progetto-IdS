package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.create.CreatePacchettoRequest;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.ForbiddenException;
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
            return pacchettoRepo.findById(id).orElseThrow();
        }

        if (isDistributore(u)) {
            return pacchettoRepo
                    .findByIdAndDistributoreId(id, u.getId());
        }

        throw new ForbiddenException("Accesso non consentito");
    }

    public Pacchetto crea(CreatePacchettoRequest req) {
        UtenteGenerico u = getUtenteLoggato();

        if (!isDistributore(u) && !isGestorePiattaforma(u)) {
            throw new ForbiddenException("Solo distributori o gestore");
        }

        Pacchetto p = new Pacchetto();
        p.setNome(req.getNome());
        p.setPrezzo(req.getPrezzo());

        if (isDistributore(u)) {
            p.setDistributore((DistributoreTipicita) u);
        }

        List<Prodotto> prodotti = new ArrayList<>();
        if (req.getProdottiIds() != null) {
            for (Long pid : req.getProdottiIds()) {
                prodotti.add(prodottoRepo.findById(pid).orElseThrow());
            }
        }
        p.setProdotti(prodotti);

        return pacchettoRepo.save(p);
    }

    public void elimina(Long id) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            pacchettoRepo.deleteById(id);
            return;
        }

        if (isDistributore(u)) {
            if (!pacchettoRepo.existsByIdAndDistributoreId(id, u.getId())) {
                throw new ForbiddenException("Pacchetto non tuo");
            }
            pacchettoRepo.deleteById(id);
            return;
        }

        throw new ForbiddenException("Accesso non consentito");
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
