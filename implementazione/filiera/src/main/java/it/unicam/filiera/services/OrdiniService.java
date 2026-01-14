package it.unicam.filiera.services;

import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.ordine.Ordine;
import it.unicam.filiera.ordine.StatoOrdine;
import it.unicam.filiera.prodotto.Pacchetto;
import it.unicam.filiera.repositories.AcquirenteRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.PacchettoRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
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

        Ordine ordine = new Ordine(); // JPA vuole costruttore no-args :contentReference[oaicite:3]{index=3}
        ordine.setAcquirente(acquirente);
        ordine.setPacchetti(pacchetti);
        ordine.setTotale(totale);
        ordine.setStato(StatoOrdine.CREATO);

        return ordineRepository.save(ordine); // CrudRepository#save :contentReference[oaicite:4]{index=4}
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

    /**
     * Prova a leggere il "prezzo" del pacchetto anche se il getter nel tuo progetto
     * si chiama diversamente (es: getCostoTotale, getPrezzoTotale, getTotale, ...).
     */
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
