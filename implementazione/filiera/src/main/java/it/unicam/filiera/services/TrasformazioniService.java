package it.unicam.filiera.services;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.dto.create.CreateTrasformazioneRequest;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrasformazioniService {

    private final TrasformazioniProdottiRepository trasformazioniRepo;
    private final ProdottiRepository prodottoRepo;
    private final UtentiRepository utentiRepo;
    private final  AnnunciProdottiRepository annunciProdottiRepository;

    public TrasformazioniService(TrasformazioniProdottiRepository trasformazioniRepo,
                                 ProdottiRepository prodottoRepo,
                                 UtentiRepository utentiRepo, AnnunciProdottiRepository annunciProdottiRepository) {
        this.trasformazioniRepo = trasformazioniRepo;
        this.prodottoRepo = prodottoRepo;
        this.utentiRepo = utentiRepo;
        this.annunciProdottiRepository = annunciProdottiRepository;
    }

    public TrasformazioneProdotto creaTrasformazione(CreateTrasformazioneRequest req) {

        UtenteGenerico u = getUtenteLoggato();

        if (!(u instanceof Trasformatore trasformatore)) {
            throw new ForbiddenException("Solo un trasformatore può creare trasformazioni");
        }

        Prodotto input = prodottoRepo.findById(req.inputId())
                .orElseThrow(() -> new NotFoundException("Prodotto di input non trovato"));

        if (!input.getVendibile()) {
            throw new BadRequestException(
                    "Il prodotto non è certificato/approvato e non può essere trasformato"
            );
        }

        if (annunciProdottiRepository.existsByProdotto_Id(input.getId())) {
            throw new BadRequestException(
                    "Il prodotto è già presente su marketplace e non può essere eliminato"
            );
        }

        // Creo prodotto output
        Prodotto output = new Prodotto();
        output.setProprietario(trasformatore);
        output.setCategoria(input.getCategoria());
        output.setPrezzo(input.getPrezzo());
        output.setNome(req.nuovoNomeOutput() != null ? req.nuovoNomeOutput() : input.getNome());
        output.setVendibile(false);
        output.setIsTrasformato(true);

        prodottoRepo.save(output);

        TrasformazioneProdotto trasformazione = new TrasformazioneProdotto(
                trasformatore,
                input,
                output,
                req.note()
        );

        return trasformazioniRepo.save(trasformazione);
    }


    public List<TrasformazioneProdotto> listaPerTrasformatore(Long trasformatoreId) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            return trasformazioniRepo.findByTrasformatoreId(trasformatoreId);
        }

        if (isTrasformatore(u)) {
            if (!((Trasformatore) u).getId().equals(trasformatoreId)) {
                throw new ForbiddenException("Non puoi vedere le trasformazioni di altri trasformatori");
            }
            return trasformazioniRepo.findByTrasformatoreId(trasformatoreId);
        }

        throw new ForbiddenException("Accesso non consentito");
    }

    public List<TrasformazioneProdotto> mieTrasformazioni() {
        UtenteGenerico u = getUtenteLoggato();

        if (!(u instanceof Trasformatore trasformatore)) {
            throw new ForbiddenException("Accesso non consentito");
        }

        return trasformazioniRepo.findByTrasformatoreId(trasformatore.getId());
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

    private boolean isTrasformatore(UtenteGenerico u) {
        return u instanceof Trasformatore;
    }
}
