package it.unicam.filiera.services;

import java.util.List;

import it.unicam.filiera.dto.create.CreateProdottoRequest;
import it.unicam.filiera.dto.response.ProdottoResponse;
import it.unicam.filiera.dto.update.UpdateProdottoRequest;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.AnnunciProdottiRepository;
import it.unicam.filiera.repositories.PacchettiRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.ProdottiRepository;

@Service
public class ProdottiService {

    private final ProdottiRepository prodottiRepository;
    private final PacchettiRepository pacchettiRepository;
    private final AnnunciProdottiRepository annunciProdottiRepository;

    public ProdottiService(ProdottiRepository prodottiRepository, PacchettiRepository pacchettiRepository, AnnunciProdottiRepository annunciProdottiRepository) {
        this.prodottiRepository = prodottiRepository;
        this.pacchettiRepository = pacchettiRepository;
        this.annunciProdottiRepository = annunciProdottiRepository;
    }

    public List<ProdottoResponse> all() {
        UtenteGenerico u = getUtenteLoggato();
        List<Prodotto> prodotti;

        if (isGestorePiattaforma(u)) {
            prodotti = prodottiRepository.findAll();
        } else if (u instanceof Azienda) {
            prodotti = prodottiRepository.findByProprietario((Azienda) u);
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
            p = prodottiRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else if (isAzienda(u)) {
            p = prodottiRepository.findByIdAndProprietario(id, (Azienda) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        return ProdottoResponse.from(p);
    }

    public ProdottoResponse crea(CreateProdottoRequest dto) {
        UtenteGenerico u = getUtenteLoggato();

        if (isAzienda(u)) {
            Prodotto p = new Prodotto();
            p.setNome(dto.nome());
            p.setCategoria(dto.categoria());
            p.setProprietario((Azienda) u);

            Prodotto saved = prodottiRepository.save(p);
            return ProdottoResponse.from(saved);
        }


        throw new ForbiddenException("Solo aziende possono creare prodotti");
    }

    public ProdottoResponse patch(Long id, UpdateProdottoRequest dto) {
        UtenteGenerico u = getUtenteLoggato();
        Prodotto existing;

        if (isGestorePiattaforma(u)) {
            existing = prodottiRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else if (isAzienda(u)) {
            existing = prodottiRepository.findByIdAndProprietario(id, (Azienda) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        checkProdottoNonVincolato(existing.getId());

        if (dto.nome() != null) {
            existing.setNome(dto.nome());
        }
        if (dto.categoria() != null) {
            existing.setCategoria(dto.categoria());
        }

        return ProdottoResponse.from(prodottiRepository.save(existing));    }

    public void delete(Long id) {
        UtenteGenerico u = getUtenteLoggato();
        Prodotto existing;

        if (isGestorePiattaforma(u)) {
            existing = prodottiRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else if (isAzienda(u)) {
            existing = prodottiRepository.findByIdAndProprietario(id, (Azienda) u)
                    .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        checkProdottoNonVincolato(existing.getId());

        prodottiRepository.delete(existing);
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

    private boolean isAzienda(UtenteGenerico u) {
        return u instanceof Azienda;
    }

    private void checkProdottoNonVincolato(Long prodottoId) {
        if (pacchettiRepository.existsByProdotti_Id(prodottoId)) {
            throw new ForbiddenException(
                    "Il prodotto è incluso in un pacchetto e non può essere modificato/eliminato"
            );
        }

        if (annunciProdottiRepository.existsByProdotto_Id(prodottoId)) {
            throw new ForbiddenException(
                    "Il prodotto è già presente sul marketplace e non può essere modificato/eliminato"
            );
        }
    }

}
