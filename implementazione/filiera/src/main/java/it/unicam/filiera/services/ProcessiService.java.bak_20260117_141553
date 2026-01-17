package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreateProcessoRequest;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.repositories.ProdottoRepository;
import it.unicam.filiera.repositories.ProcessoTrasformazioneRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessiService {

    private final ProcessoTrasformazioneRepository processiRepo;
    private final TrasformatoreRepository trasformatoreRepo;
    private final ProdottoRepository prodottoRepo;

    public ProcessiService(ProcessoTrasformazioneRepository processiRepo,
                           TrasformatoreRepository trasformatoreRepo,
                           ProdottoRepository prodottoRepo) {
        this.processiRepo = processiRepo;
        this.trasformatoreRepo = trasformatoreRepo;
        this.prodottoRepo = prodottoRepo;
    }

    @Transactional
    public ProcessoTrasformazione crea(CreateProcessoRequest request) {
        Trasformatore t = trasformatoreRepo.findById(request.getTrasformatoreId())
                .orElseThrow(() -> new NotFoundException("Trasformatore non trovato: " + request.getTrasformatoreId()));

        List<Prodotto> input = prodottoRepo.findAllById(request.getInputProdottiIds());
        if (input.isEmpty()) {
            throw new NotFoundException("Input prodotti vuoto o non trovato");
        }

        ProcessoTrasformazione p = new ProcessoTrasformazione();
        p.setDescrizione(request.getDescrizione());
        p.setTrasformatore(t);
        p.setInputProdotti(input);

        if (request.getOutputProdottoId() != null) {
            Prodotto out = prodottoRepo.findById(request.getOutputProdottoId())
                    .orElseThrow(() -> new NotFoundException("Prodotto output non trovato: " + request.getOutputProdottoId()));
            p.setOutputProdotto(out);
        }

        return processiRepo.save(p);
    }

    @Transactional
    public ProcessoTrasformazione chiudi(Long id) {
        ProcessoTrasformazione p = processiRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Processo non trovato: " + id));
        p.chiudi();
        return processiRepo.save(p);
    }

    public List<ProcessoTrasformazione> lista() {
        return processiRepo.findAll();
    }
}
