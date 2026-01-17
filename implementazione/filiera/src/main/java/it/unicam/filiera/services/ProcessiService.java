package it.unicam.filiera.services;

import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.repositories.ProcessoTrasformazioneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessiService {

    private final ProcessoTrasformazioneRepository processoRepo;

    public ProcessiService(ProcessoTrasformazioneRepository processoRepo) {
        this.processoRepo = processoRepo;
    }

    public ProcessoTrasformazione crea(String descrizione) {
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("descrizione obbligatoria");
        return processoRepo.save(new ProcessoTrasformazione(descrizione));
    }

    public List<ProcessoTrasformazione> lista() {
        return processoRepo.findAll();
    }

    public ProcessoTrasformazione chiudi(Long id) {
        ProcessoTrasformazione p = processoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo non trovato: " + id));
        p.chiudi();
        return processoRepo.save(p);
    }
}