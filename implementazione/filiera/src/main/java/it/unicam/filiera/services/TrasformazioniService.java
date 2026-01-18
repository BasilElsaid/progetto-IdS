package it.unicam.filiera.services;

import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.ProcessoTrasformazioneRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import it.unicam.filiera.repositories.TrasformazioneProdottoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrasformazioniService {

    private final TrasformazioneProdottoRepository trasformazioniRepo;
    private final ProcessoTrasformazioneRepository processiRepo;
    private final ProdottoRepository prodottoRepo;
    private final TrasformatoreRepository trasformatoreRepo;

    public TrasformazioniService(TrasformazioneProdottoRepository trasformazioniRepo,
                                 ProcessoTrasformazioneRepository processiRepo,
                                 ProdottoRepository prodottoRepo,
                                 TrasformatoreRepository trasformatoreRepo) {
        this.trasformazioniRepo = trasformazioniRepo;
        this.processiRepo = processiRepo;
        this.prodottoRepo = prodottoRepo;
        this.trasformatoreRepo = trasformatoreRepo;
    }

    public TrasformazioneProdotto creaTrasformazione(
            Long processoId,
            Long trasformatoreId,
            Long inputId,
            Long outputId,
            Double quantitaInput,
            Double quantitaOutput,
            String note
    ) {

        UtenteGenerico u = getUtenteLoggato();

        if (isTrasformatore(u)) {
            Trasformatore tLoggato = (Trasformatore) u;
            if (!tLoggato.getId().equals(trasformatoreId)) {
                throw new ForbiddenException("Non puoi creare trasformazioni per altri trasformatori");
            }
        }
        // gestore piattaforma → libero

        ProcessoTrasformazione processo = processiRepo.findById(processoId).orElseThrow();
        Trasformatore trasformatore = trasformatoreRepo.findById(trasformatoreId).orElseThrow();
        Prodotto input = prodottoRepo.findById(inputId).orElseThrow();
        Prodotto output = prodottoRepo.findById(outputId).orElseThrow();

        TrasformazioneProdotto t = new TrasformazioneProdotto(
                processo,
                trasformatore,
                input,
                output,
                quantitaInput,
                quantitaOutput,
                note
        );

        return trasformazioniRepo.save(t);
    }

    public List<TrasformazioneProdotto> listaPerProcesso(Long processoId) {
        UtenteGenerico u = getUtenteLoggato();

        if (isGestorePiattaforma(u)) {
            return trasformazioniRepo.findByProcessoId(processoId);
        }

        if (isTrasformatore(u)) {
            return trasformazioniRepo.findByProcessoIdAndTrasformatoreId(
                    processoId,
                    ((Trasformatore) u).getId()
            );
        }

        throw new ForbiddenException("Accesso non consentito");
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
