package it.unicam.filiera.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.repositories.TrasformazioneProdottoRepository;

import it.unicam.filiera.repositories.ProcessoTrasformazioneRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;

import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.Trasformatore;

@Service
public class TrasformazioniService {

    private final TrasformazioneProdottoRepository repo;
    private final ProcessoTrasformazioneRepository processoRepo;
    private final ProdottoRepository prodottoRepo;
    private final TrasformatoreRepository trasformatoreRepo;

    public TrasformazioniService(TrasformazioneProdottoRepository repo,
                                 ProcessoTrasformazioneRepository processoRepo,
                                 ProdottoRepository prodottoRepo,
                                 TrasformatoreRepository trasformatoreRepo) {
        this.repo = repo;
        this.processoRepo = processoRepo;
        this.prodottoRepo = prodottoRepo;
        this.trasformatoreRepo = trasformatoreRepo;
    }

    @Transactional
    public TrasformazioneProdotto creaTrasformazione(Long processoId, Long trasformatoreId,
                                                     Long inputId, Long outputId,
                                                     Double quantitaInput, Double quantitaOutput,
                                                     String note) {
        ProcessoTrasformazione processo = processoRepo.findById(processoId).orElseThrow();
        Trasformatore trasformatore = trasformatoreRepo.findById(trasformatoreId).orElseThrow();
        Prodotto inP = prodottoRepo.findById(inputId).orElseThrow();
        Prodotto outP = prodottoRepo.findById(outputId).orElseThrow();

        TrasformazioneProdotto t = new TrasformazioneProdotto();
        t.setProcesso(processo);
        t.setTrasformatore(trasformatore);
        t.setProdottoInput(inP);
        t.setProdottoOutput(outP);
        t.setQuantitaInput(quantitaInput);
        t.setQuantitaOutput(quantitaOutput);
        t.setNote(note);

        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<TrasformazioneProdotto> listaPerProcesso(Long processoId) {
        return repo.findByProcessoId(processoId);
    }

    @Transactional(readOnly = true)
    public List<TrasformazioneProdotto> listaPerTrasformatore(Long trasformatoreId) {
        return repo.findByTrasformatoreId(trasformatoreId);
    }
}