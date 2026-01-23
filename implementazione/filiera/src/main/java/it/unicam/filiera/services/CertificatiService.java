package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.certificati.*;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.CertificatoCuratoreRepository;
import it.unicam.filiera.repositories.CertificatoRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CertificatiService {

    private final CertificatoRepository certificatoRepo;
    private final ProdottoRepository prodottoRepo;
    private final CertificatoCuratoreRepository curatoreRepo;

    public CertificatiService(CertificatoRepository certificatoRepo,
                              CertificatoCuratoreRepository curatoreRepo,
                              ProdottoRepository prodottoRepo) {
        this.certificatoRepo = certificatoRepo;
        this.curatoreRepo = curatoreRepo;
        this.prodottoRepo = prodottoRepo;
    }

    // --- CREATE ---
    public Certificato creaCertificato(CreateCertificatoRequest dto) {
        UtenteGenerico u = getUtenteLoggato();
        TipoCertificatore consentito = tipoConsentito(u);

        if (consentito != null && dto.tipo != consentito) {
            throw new ForbiddenException("Non puoi creare certificati di tipo " + dto.tipo);
        }

        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

        if (certificatoRepo.existsByProdottoIdAndTipo(dto.prodottoId, dto.tipo)) {
            throw new BadRequestException("Esiste già un certificato di tipo " + dto.tipo + " per questo prodotto");
        }

        validaDto(dto);
        Certificato c = inizializzaCertificato(dto, p);

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

    public Certificato aggiornaCertificato(Long id, CreateCertificatoRequest dto) {
        Certificato c = getCertificato(id);
        checkOwnership(c);

        if (!c.getProdotto().getId().equals(dto.prodottoId)) {
            throw new BadRequestException("Non è consentito cambiare il prodotto del certificato");
        }

        c.setProdotto(prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato")));

        validaDto(dto);
        assegnaCampiCertificato(c, dto);

        return certificatoRepo.save(c);
    }

    public void eliminaCertificato(Long id) {
        Certificato c = getCertificato(id);
        checkOwnership(c);
        certificatoRepo.delete(c);
    }

    // --- VERIFICA CERTIFICATO ---
    public boolean verificaCertificato(Long certificatoId, Boolean approvato, String commento) {
        UtenteGenerico u = getUtenteLoggato();
        if (tipoConsentito(u) != TipoCertificatore.CURATORE) {
            throw new ForbiddenException("Solo il curatore può verificare certificati");
        }

        Certificato target = certificatoRepo.findById(certificatoId)
                .orElseThrow(() -> new NotFoundException("Certificato target non trovato"));

        if (target instanceof CertificatoCuratore) {
            throw new BadRequestException("Non è possibile verificare un altro certificato curatore");
        }

        CertificatoCuratore cc = new CertificatoCuratore();
        cc.setCertificatoTarget(target);
        cc.setProdotto(target.getProdotto());
        cc.setApprovato(approvato);
        cc.setCommento(commento);
        cc.setTipo(TipoCertificatore.CURATORE);

        curatoreRepo.save(cc);
        return cc.isApprovato();
    }

    // --- VALIDAZIONE ---
    private void validaDto(CreateCertificatoRequest dto) {
        switch(dto.tipo) {
            case PRODUTTORE -> {
                if(dto.azienda == null || dto.origineMateriaPrima == null)
                    throw new BadRequestException("Campi azienda e origineMateriaPrima obbligatori per PRODUTTORE");
                if(dto.processo != null || dto.impianto != null || dto.approvato != null || dto.commento != null)
                    throw new BadRequestException("Campi non validi presenti per PRODUTTORE");
            }
            case TRASFORMATORE -> {
                if(dto.processo == null || dto.impianto == null)
                    throw new BadRequestException("Campi processo e impianto obbligatori per TRASFORMATORE");
                if(dto.azienda != null || dto.origineMateriaPrima != null || dto.approvato != null || dto.commento != null)
                    throw new BadRequestException("Campi non validi presenti per TRASFORMATORE");
            }
            case CURATORE -> {
                if(dto.approvato == null || dto.commento == null)
                    throw new BadRequestException("Campi approvato e commento obbligatori per CURATORE");
                if(dto.azienda != null || dto.origineMateriaPrima != null || dto.processo != null || dto.impianto != null)
                    throw new BadRequestException("Campi non validi presenti per CURATORE");
            }
        }
    }

    // --- HELPERS ---
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

    private Certificato inizializzaCertificato(CreateCertificatoRequest dto, Prodotto p) {
        switch (dto.tipo) {
            case PRODUTTORE: {
                CertificazioneProduttore cp = new CertificazioneProduttore();
                cp.setProdotto(p);
                cp.setAzienda(dto.azienda);
                cp.setOrigineMateriaPrima(dto.origineMateriaPrima);
                cp.setTipo(TipoCertificatore.PRODUTTORE);
                return cp;
            }
            case TRASFORMATORE: {
                CertificatoTrasformatore ct = new CertificatoTrasformatore();
                ct.setProdotto(p);
                ct.setProcesso(dto.processo);
                ct.setImpianto(dto.impianto);
                ct.setTipo(TipoCertificatore.TRASFORMATORE);
                return ct;
            }
            case CURATORE: {
                CertificatoCuratore cc = new CertificatoCuratore();
                Certificato target = certificatoRepo.findById(dto.certificatoTargetId)
                        .orElseThrow(() -> new NotFoundException("Certificato target non trovato"));

                if (target.getTipo() == TipoCertificatore.CURATORE) {
                    throw new BadRequestException("Un curatore non può approvare un altro curatore");
                }

                cc.setCertificatoTarget(target);
                cc.setProdotto(target.getProdotto());
                cc.setApprovato(dto.approvato);
                cc.setCommento(dto.commento);
                cc.setTipo(TipoCertificatore.CURATORE);
                return cc;
            }
            default:
                throw new BadRequestException("Tipo certificatore non valido");
        }
    }

    private void assegnaCampiCertificato(Certificato c, CreateCertificatoRequest dto) {
        switch(dto.tipo) {
            case PRODUTTORE -> {
                if(c instanceof CertificazioneProduttore cp) {
                    cp.setAzienda(dto.azienda);
                    cp.setOrigineMateriaPrima(dto.origineMateriaPrima);
                }
            }
            case TRASFORMATORE -> {
                if(c instanceof CertificatoTrasformatore ct) {
                    ct.setProcesso(dto.processo);
                    ct.setImpianto(dto.impianto);
                }
            }
            case CURATORE -> {
                if(c instanceof CertificatoCuratore cc) {
                    cc.setApprovato(dto.approvato);
                    cc.setCommento(dto.commento);
                }
            }
        }
        c.setTipo(dto.tipo);
    }
}