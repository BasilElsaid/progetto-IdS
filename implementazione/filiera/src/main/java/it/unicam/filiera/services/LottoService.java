package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.*;
import it.unicam.filiera.domain.Lotto;
import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.enums.AuditAction;
import it.unicam.filiera.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LottoService {

    private final LottoRepository lottoRepo;
    private final ProdottoRepository prodottoRepo;
    private final AziendaRepository aziendaRepo;
    private final TrasformazioneProdottoRepository trasformazioneRepo;
    private final OrdineRepository ordineRepo;
    private final AuditService auditService;

    public LottoService(LottoRepository lottoRepo,
                        ProdottoRepository prodottoRepo,
                        AziendaRepository aziendaRepo,
                        TrasformazioneProdottoRepository trasformazioneRepo,
                        OrdineRepository ordineRepo,
                        AuditService auditService) {
        this.lottoRepo = lottoRepo;
        this.prodottoRepo = prodottoRepo;
        this.aziendaRepo = aziendaRepo;
        this.trasformazioneRepo = trasformazioneRepo;
        this.ordineRepo = ordineRepo;
        this.auditService = auditService;
    }

    public LottoResponse creaLotto(CreateLottoRequest req) {
        if (req.getProdottoId() == null || req.getAziendaId() == null) {
            throw new BadRequestException("prodottoId e aziendaId obbligatori");
        }
        if (req.getQuantita() == null || req.getQuantita() <= 0) {
            throw new BadRequestException("quantita deve essere > 0");
        }
        if (req.getDataRaccolta() == null) {
            throw new BadRequestException("dataRaccolta obbligatoria");
        }

        UtenteGenerico user = getUtenteLoggato();
        if (!(user instanceof Azienda) && user.getRuolo() != Ruolo.GESTORE_PIATTAFORMA) {
            throw new ForbiddenException("Solo aziende o gestore piattaforma possono creare lotti");
        }
        if (user instanceof Azienda a && user.getRuolo() != Ruolo.GESTORE_PIATTAFORMA) {
            if (!a.getId().equals(req.getAziendaId())) {
                throw new ForbiddenException("Non puoi creare un lotto per un'altra azienda");
            }
        }

        var prodotto = prodottoRepo.findById(req.getProdottoId())
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
        var azienda = aziendaRepo.findById(req.getAziendaId())
                .orElseThrow(() -> new NotFoundException("Azienda non trovata"));

        String qr = "LOT-" + UUID.randomUUID().toString().replace("-", "");
        Lotto lotto = new Lotto(qr, prodotto, azienda, req.getQuantita(), req.getUnitaMisura(), req.getDataRaccolta(), req.getNote());

        Lotto saved = lottoRepo.save(lotto);

        auditService.log("Lotto", saved.getId(), AuditAction.CREATE,
                "Creato lotto " + saved.getQrCode() + " per prodottoId=" + prodotto.getId() + " quantita=" + saved.getQuantita() + saved.getUnitaMisura());

        return toResponse(saved);
    }

    public LottoResponse getLotto(Long id) {
        Lotto lotto = lottoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Lotto non trovato"));
        return toResponse(lotto);
    }

    public LottoTimelineResponse timeline(Long id) {
        Lotto lotto = lottoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Lotto non trovato"));
        return buildTimeline(lotto);
    }

    public LottoTimelineResponse publicByQrCode(String qrCode) {
        Lotto lotto = lottoRepo.findByQrCode(qrCode)
                .orElseThrow(() -> new NotFoundException("Lotto non trovato"));
        return buildTimeline(lotto);
    }

    private LottoTimelineResponse buildTimeline(Lotto lotto) {
        List<TimelineItemResponse> items = new ArrayList<>();

        // 1) PRODUZIONE
        TimelineItemResponse prod = new TimelineItemResponse();
        prod.setTipo("PRODUZIONE");
        prod.setDataOra(lotto.getCreatoIl());
        prod.setDescrizione("Creato lotto (raccolta: " + lotto.getDataRaccolta() + ") - quantita " + lotto.getQuantita() + " " + lotto.getUnitaMisura());
        prod.setRefTipo("Lotto");
        prod.setRefId(lotto.getId());
        items.add(prod);

        // 2) TRASFORMAZIONI (collegate per prodotto input/output)
        List<TrasformazioneProdotto> trasformazioni = trasformazioneRepo
                .findByInputProdottoIdOrOutputProdottoIdOrderByCreatoIlAsc(lotto.getProdotto().getId(), lotto.getProdotto().getId());
        for (TrasformazioneProdotto t : trasformazioni) {
            TimelineItemResponse tr = new TimelineItemResponse();
            tr.setTipo("TRASFORMAZIONE");
            tr.setDataOra(t.getCreatoIl());
            tr.setDescrizione(
                    "Processo #" + t.getProcesso().getId() + ": " +
                            t.getInputProdotto().getNome() + " -> " + t.getOutputProdotto().getNome() +
                            " (" + t.getQuantitaInput() + " -> " + t.getQuantitaOutput() + ")"
            );
            tr.setRefTipo("Trasformazione");
            tr.setRefId(t.getId());
            items.add(tr);
        }

        // 3) VENDITE (ordini dove compare il prodotto del lotto)
        List<Ordine> ordini = ordineRepo.findDistinctByProdotti_IdOrderByDataOraAsc(lotto.getProdotto().getId());
        for (Ordine o : ordini) {
            TimelineItemResponse v = new TimelineItemResponse();
            v.setTipo("VENDITA");
            v.setDataOra(o.getDataOra());
            String buyer = (o.getAcquirente() != null) ? o.getAcquirente().getUsername() : "?";
            v.setDescrizione("Ordine #" + o.getId() + " - acquirente: " + buyer + " - stato: " + o.getStato());
            v.setRefTipo("Ordine");
            v.setRefId(o.getId());
            items.add(v);
        }

        items.sort((a, b) -> {
            LocalDateTime da = a.getDataOra() != null ? a.getDataOra() : LocalDateTime.MIN;
            LocalDateTime db = b.getDataOra() != null ? b.getDataOra() : LocalDateTime.MIN;
            return da.compareTo(db);
        });

        LottoTimelineResponse resp = new LottoTimelineResponse();
        resp.setLotto(toResponse(lotto));
        resp.setTimeline(items);
        return resp;
    }

    private LottoResponse toResponse(Lotto lotto) {
        LottoResponse r = new LottoResponse();
        r.setId(lotto.getId());
        r.setQrCode(lotto.getQrCode());

        r.setProdottoId(lotto.getProdotto().getId());
        r.setProdottoNome(lotto.getProdotto().getNome());
        r.setProdottoCategoria(lotto.getProdotto().getCategoria());

        r.setAziendaId(lotto.getAzienda().getId());
        r.setAziendaNome(lotto.getAzienda().getNomeAzienda());

        r.setQuantita(lotto.getQuantita());
        r.setUnitaMisura(lotto.getUnitaMisura());
        r.setDataRaccolta(lotto.getDataRaccolta());
        r.setCreatoIl(lotto.getCreatoIl());
        r.setNote(lotto.getNote());
        return r;
    }

    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return user;
    }
}
