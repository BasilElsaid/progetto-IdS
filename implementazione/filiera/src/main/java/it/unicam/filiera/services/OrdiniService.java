package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.PacchettoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdiniService {

    private final OrdineRepository ordineRepository;
    private final AcquirenteRepository acquirenteRepository;
    private final PacchettoRepository pacchettiRepo;

    public OrdiniService(OrdineRepository ordineRepository,
                         AcquirenteRepository acquirenteRepository,
                         PacchettoRepository pacchettiRepo) {
        this.ordineRepository = ordineRepository;
        this.acquirenteRepository = acquirenteRepository;
        this.pacchettiRepo = pacchettiRepo;
    }

    public Ordine creaOrdine(Long acquirenteId, List<Long> pacchettoIds) {
        if (acquirenteId == null) {
            throw new BadRequestException("acquirenteId mancante");
        }
        if (pacchettoIds == null || pacchettoIds.isEmpty()) {
            throw new BadRequestException("pacchettoIds mancanti");
        }

        Acquirente acquirente = acquirenteRepository.findById(acquirenteId)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        List<Pacchetto> pacchetti = (List<Pacchetto>) pacchettiRepo.findAllById(pacchettoIds);

        double totale = pacchetti.stream()
                .mapToDouble(this::estraiPrezzoPacchetto)
                .sum();

        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        ordine.setPacchetti(pacchetti);
        ordine.setTotale(totale);
        ordine.setStato(StatoOrdine.CREATO);

        return ordineRepository.save(ordine);
    }

    public Ordine getById(Long id) {
        return ordineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));
    }

    public List<Ordine> all() {
        return (List<Ordine>) ordineRepository.findAll();
    }

    public List<Ordine> getByAcquirente(Long acquirenteId) {
        return ordineRepository.findByAcquirenteId(acquirenteId);
    }


    @Transactional
    public Ordine aggiornaStato(Long ordineId, StatoOrdine nuovoStato, String trackingCode) {
        if (ordineId == null) throw new BadRequestException("ordineId mancante");
        if (nuovoStato == null) throw new BadRequestException("stato mancante");

        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (nuovoStato == StatoOrdine.SPEDITO && ordine.getStato() != StatoOrdine.PAGATO) {
            throw new BadRequestException("Puoi spedire solo ordini PAGATI");
        }
        if (nuovoStato == StatoOrdine.CONSEGNATO && ordine.getStato() != StatoOrdine.SPEDITO) {
            throw new BadRequestException("Puoi consegnare solo ordini SPEDITI");
        }

        ordine.setStato(nuovoStato);

        LocalDateTime now = LocalDateTime.now();

        if (nuovoStato == StatoOrdine.SPEDITO) {
            invocaSetterSeEsiste(ordine, "setDataSpedizione", LocalDateTime.class, now);
            invocaSetterSeEsiste(ordine, "setDataStimataConsegnaDa", LocalDateTime.class, now.plusDays(3));
            invocaSetterSeEsiste(ordine, "setDataStimataConsegnaA", LocalDateTime.class, now.plusDays(4));

            if (trackingCode != null && !trackingCode.isBlank()) {
                invocaSetterSeEsiste(ordine, "setTrackingCode", String.class, trackingCode.trim());
            }
        }

        if (nuovoStato == StatoOrdine.CONSEGNATO) {
            invocaSetterSeEsiste(ordine, "setDataConsegna", LocalDateTime.class, now);
        }

        return ordineRepository.save(ordine);
    }

    private void invocaSetterSeEsiste(Object target, String methodName, Class<?> paramType, Object value) {
        try {
            Method m = target.getClass().getMethod(methodName, paramType);
            m.invoke(target, value);
        } catch (Exception ignored) {
        }
    }


    private double estraiPrezzoPacchetto(Pacchetto p) {
        for (String nomeMetodo : new String[]{"getPrezzo", "getCostoTotale", "getPrezzoTotale", "getTotale", "getCosto"}) {
            try {
                Method m = p.getClass().getMethod(nomeMetodo);
                Object v = m.invoke(p);
                if (v instanceof Number n) return n.doubleValue();
            } catch (Exception ignored) {
            }
        }
        throw new BadRequestException("Pacchetto: impossibile determinare il prezzo (manca un getter tipo getPrezzo/getCostoTotale/...)");
    }
}
