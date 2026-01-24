package it.unicam.filiera.services;

import java.util.List;

import it.unicam.filiera.dto.create.CreateProdottoRequest;
import it.unicam.filiera.dto.response.ProdottoResponse;
import it.unicam.filiera.dto.update.UpdateProdottoRequest;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.UtenteGenerico;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.ProdottoRepository;

@Service
public class ProdottiService {

    private final ProdottoRepository repo;

    public ProdottiService(ProdottoRepository repo) {
        this.repo = repo;
    }

    public List<ProdottoResponse> all() {
        UtenteGenerico u = getUtenteLoggato();
        List<Prodotto> prodotti;

        if (isGestorePiattaforma(u)) {
            prodotti = repo.findAll();
        } else if (isProduttore(u)) {
            prodotti = repo.findByProduttore((Produttore) u);
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        return prodotti.stream()
                .map(ProdottoResponse::from)
                .toList();
    }

    public ProdottoResponse get(Long id) {
        UtenteGenerico u = getUtenteLoggato();
        Prodotto p;

        if (isGestorePiattaforma(u)) {
            p = repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else if (isProduttore(u)) {
            p = repo.findByIdAndProduttore(id, (Produttore) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        return ProdottoResponse.from(repo.save(p));
    }

    public ProdottoResponse crea(CreateProdottoRequest dto) {
        UtenteGenerico u = getUtenteLoggato();

        if (isProduttore(u)) {
            Prodotto p = new Prodotto();
            p.setNome(dto.nome());
            p.setCategoria(dto.categoria());
            p.setProduttore((Produttore) u);

            Prodotto saved = repo.save(p);
            return ProdottoResponse.from(repo.save(saved));
        }


        throw new ForbiddenException("Solo produttori possono creare prodotti");
    }

    public ProdottoResponse patch(Long id, UpdateProdottoRequest dto) {
        UtenteGenerico u = getUtenteLoggato();
        Prodotto existing;

        if (isGestorePiattaforma(u)) {
            existing = repo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else if (isProduttore(u)) {
            existing = repo.findByIdAndProduttore(id, (Produttore) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        if (dto.nome() != null) {
            existing.setNome(dto.nome());
        }
        if (dto.categoria() != null) {
            existing.setCategoria(dto.categoria());
        }

        return ProdottoResponse.from(repo.save(existing));    }

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
