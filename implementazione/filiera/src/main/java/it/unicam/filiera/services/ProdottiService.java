package it.unicam.filiera.services;

import java.util.List;

import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.UtenteGenerico;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.ProdottoRepository;

@Service
public class ProdottiService {

    private final ProdottoRepository repo;

    public List<Prodotto> list(String nome, String categoria) {
        return repo.findAll();
    }

    public ProdottiService(ProdottoRepository repo) {
        this.repo = repo;
    }

    public List<Prodotto> all() {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            return repo.findAll(); // vede tutti i prodotti
        } else if (isProduttore(u)) {
            return repo.findByProduttore((Produttore) u); // solo i suoi
        }

        throw new ForbiddenException("Ruolo non autorizzato");
    }

    public Prodotto get(Long id) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            return repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else if (isProduttore(u)) {
            return repo.findByIdAndProduttore(id, (Produttore) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        }

        throw new ForbiddenException("Ruolo non autorizzato");
    }

    public Prodotto crea(Prodotto p) {
        UtenteGenerico u = getUtenteLoggato();

        if (isProduttore(u)) {
            p.setProduttore((Produttore) u);
            return repo.save(p);
        }

        throw new ForbiddenException("Solo produttori possono creare prodotti");
    }

    public Prodotto update(Long id, Prodotto p) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            Prodotto existing = repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
            existing.setNome(p.getNome());
            existing.setCategoria(p.getCategoria());
            existing.setPrezzo(p.getPrezzo());
            return repo.save(existing);
        } else if (isProduttore(u)) {
            Prodotto existing = repo.findByIdAndProduttore(id, (Produttore) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
            existing.setNome(p.getNome());
            existing.setCategoria(p.getCategoria());
            existing.setPrezzo(p.getPrezzo());
            return repo.save(existing);
        }

        throw new ForbiddenException("Ruolo non autorizzato");
    }

    public void delete(Long id) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            Prodotto existing = repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
            repo.delete(existing);
            return;
        } else if (isProduttore(u)) {
            Prodotto existing = repo.findByIdAndProduttore(id, (Produttore) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
            repo.delete(existing);
            return;
        }

        throw new ForbiddenException("Ruolo non autorizzato");
    }

    // ================= HELPERS =================
    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return user;
    }

    private boolean isGestorePiattaforma(UtenteGenerico u) {
        return u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA;
    }

    private boolean isProduttore(UtenteGenerico u) {
        return u instanceof Produttore;
    }

}
