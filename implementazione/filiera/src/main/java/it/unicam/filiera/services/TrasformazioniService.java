package it.unicam.filiera.services;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.dto.create.CreateTrasformazioneRequest;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.enums.TipoCertificatore;
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

    private final TrasformazioneProdottoRepository trasformazioniRepo;
    private final ProdottoRepository prodottoRepo;
    private final TrasformatoreRepository trasformatoreRepo;
    private final CertificatoCuratoreRepository certificatoCuratoreRepo;

    public TrasformazioniService(TrasformazioneProdottoRepository trasformazioniRepo,
                                 ProdottoRepository prodottoRepo,
                                 TrasformatoreRepository trasformatoreRepo, CertificatoCuratoreRepository certificatoCuratoreRepo) {
        this.trasformazioniRepo = trasformazioniRepo;
        this.prodottoRepo = prodottoRepo;
        this.trasformatoreRepo = trasformatoreRepo;
        this.certificatoCuratoreRepo = certificatoCuratoreRepo;
    }

    public TrasformazioneProdotto creaTrasformazione(CreateTrasformazioneRequest req) {
        UtenteGenerico u = getUtenteLoggato();

        // Solo il trasformatore può creare trasformazioni su se stesso
        if (isTrasformatore(u) && !((Trasformatore) u).getId().equals(req.trasformatoreId())) {
            throw new ForbiddenException("Non puoi creare trasformazioni per altri trasformatori");
        }

        // Recupero prodotto di input e trasformatore
        Prodotto input = prodottoRepo.findById(req.inputId())
                .orElseThrow(() -> new NotFoundException("Prodotto di input non trovato"));

        boolean approvato = certificatoCuratoreRepo.existsByCertificatoTargetProdottoIdAndApprovatoTrue(input.getId());        if (!approvato) {
            throw new BadRequestException("Prodotto di input non ha certificato produttore approvato e non può essere trasformato");
        }

        Trasformatore trasformatore = trasformatoreRepo.findById(req.trasformatoreId())
                .orElseThrow(() -> new NotFoundException("Trasformatore non trovato"));

        // Creo il prodotto di output clonando il prodotto di input
        Prodotto output = new Prodotto();
        output.setProduttore(input.getProduttore());
        output.setCategoria(input.getCategoria());
        output.setPrezzo(input.getPrezzo());
        output.setNome(req.nuovoNomeOutput() != null ? req.nuovoNomeOutput() : input.getNome());
        output.setVendibile(false);
        output.setIsTrasformato(true);

        prodottoRepo.save(output);

        // Creo la trasformazione
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
