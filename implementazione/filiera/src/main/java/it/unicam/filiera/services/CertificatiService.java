package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.certificati.*;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.CertificatiCuratoreRepository;
import it.unicam.filiera.repositories.CertificatiRepository;
import it.unicam.filiera.repositories.ProdottiRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CertificatiService {

    private final CertificatiRepository certificatoRepo;
    private final ProdottiRepository prodottoRepo;
    private final CertificatiCuratoreRepository curatoreRepo;
    private final StrategieCertificazioniFactory strategieFactory;

    public CertificatiService(CertificatiRepository certificatoRepo,
                              CertificatiCuratoreRepository curatoreRepo,
                              ProdottiRepository prodottoRepo, StrategieCertificazioniFactory strategieFactory) {
        this.certificatoRepo = certificatoRepo;
        this.curatoreRepo = curatoreRepo;
        this.prodottoRepo = prodottoRepo;
        this.strategieFactory = strategieFactory;
    }

    // CREATE
    public Certificato creaCertificato(CreateCertificatoRequest dto) {
        UtenteGenerico u = getUtenteLoggato();
        TipoCertificatore consentito = tipoConsentito(u);

        if (consentito != null && dto.tipo() != consentito) {
            throw new ForbiddenException("Non puoi creare certificati di tipo " + dto.tipo());
        }

        Prodotto p = prodottoRepo.findById(dto.prodottoId())
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

        if (certificatoRepo.existsByProdottoIdAndTipo(dto.prodottoId(), dto.tipo())) {
            throw new BadRequestException("Esiste già un certificato di tipo " + dto.tipo() + " per questo prodotto");
        }

        StrategieCertificazioni strategy = strategieFactory.getStrategia(dto.tipo());
        strategy.validaDto(dto);
        Certificato c = strategy.creaCertificato(dto, p);

        return certificatoRepo.save(c);
    }

    // --- READ ---
    public List<Certificato> getTuttiCertificati() {
        UtenteGenerico u = getUtenteLoggato();
        TipoCertificatore tipo = tipoConsentito(u);

        if (u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA || u.getRuolo() == Ruolo.CURATORE) {
            return certificatoRepo.findAll();
        }

        return certificatoRepo.findAll().stream()
                .filter(c -> puoVedereCertificato(u, c))
                .toList();
    }

    public Certificato getCertificato(Long id) {
        Certificato c = certificatoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Certificato non trovato"));
        UtenteGenerico u = getUtenteLoggato();

        if (!puoVedereCertificato(u, c)) {
            throw new ForbiddenException("Non puoi accedere a questo certificato");
        }
        return c;
    }

    public Certificato patchCertificato(Long id, UpdateCertificatoRequest dto) {
        Certificato c = getCertificato(id);
        checkOwnership(c);

        StrategieCertificazioni strategy = strategieFactory.getStrategia(c.getTipo());
        c = strategy.aggiornaCertificato(c, dto);

        return certificatoRepo.save(c);
    }

    public void eliminaCertificato(Long id) {
        Certificato c = getCertificato(id);
        checkOwnership(c);
        certificatoRepo.delete(c);
    }

    // VERIFICA CERTIFICATO
    public boolean verificaCertificato(Long certificatoId, Boolean approvato, String commento) {
        UtenteGenerico u = getUtenteLoggato();
        if (tipoConsentito(u) != TipoCertificatore.CURATORE) {
            throw new ForbiddenException("Solo il curatore può verificare certificati");
        }

        Certificato target = certificatoRepo.findById(certificatoId)
                .orElseThrow(() -> new NotFoundException("Certificato target non trovato"));

        StrategieCertificazioni strategy = strategieFactory.getStrategia(TipoCertificatore.CURATORE);

        boolean result = strategy.verifica(target, approvato, commento, curatoreRepo);

        if (approvato && (target.getTipo() == TipoCertificatore.TRASFORMATORE
                || target.getTipo() == TipoCertificatore.PRODUTTORE)) {
            Prodotto p = target.getProdotto();
            p.setVendibile(true);
            prodottoRepo.save(p);
        }

        return result;
    }

    // HELPERS
    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico u)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return (UtenteGenerico) auth.getPrincipal();
    }

    private TipoCertificatore tipoConsentito(UtenteGenerico u) {
        return switch (u.getRuolo()) {
            case PRODUTTORE -> TipoCertificatore.PRODUTTORE;
            case TRASFORMATORE -> TipoCertificatore.TRASFORMATORE;
            case CURATORE -> TipoCertificatore.CURATORE;
            case GESTORE_PIATTAFORMA -> null;
            default -> throw new ForbiddenException("Ruolo non autorizzato ai certificati");
        };
    }

    private boolean puoVedereCertificato(UtenteGenerico u, Certificato c) {
        if (u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA || u.getRuolo() == Ruolo.CURATORE) return true;

        TipoCertificatore tipo = tipoConsentito(u);
        if (tipo != null && c.getTipo() != tipo) return false;

        return (tipo == TipoCertificatore.PRODUTTORE || tipo == TipoCertificatore.TRASFORMATORE) &&
                c.getProdotto().getProduttore().getId().equals(u.getId());
    }

    private void checkOwnership(Certificato c) {
        UtenteGenerico u = getUtenteLoggato();
        TipoCertificatore tipo = tipoConsentito(u);
        if (tipo == TipoCertificatore.PRODUTTORE || tipo == TipoCertificatore.TRASFORMATORE) {
            if (!c.getProdotto().getProduttore().getId().equals(u.getId())) {
                throw new ForbiddenException("Non puoi modificare/eliminare questo certificato");
            }
        }
    }


}