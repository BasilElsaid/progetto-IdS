package it.unicam.filiera.services;

import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.repositories.ProcessoTrasformazioneRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import it.unicam.filiera.repositories.TrasformazioneProdottoRepository;
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

    public TrasformazioneProdotto creaTrasformazione(Long processoId,
                                                     Long trasformatoreId,
                                                     Long inputId,
                                                     Long outputId,
                                                     Double quantitaInput,
                                                     Double quantitaOutput,
                                                     String note) {

        ProcessoTrasformazione processo = processiRepo.findById(processoId).orElseThrow();
        Trasformatore trasformatore = trasformatoreRepo.findById(trasformatoreId).orElseThrow();
        Prodotto input = prodottoRepo.findById(inputId).orElseThrow();
        Prodotto output = prodottoRepo.findById(outputId).orElseThrow();

        TrasformazioneProdotto t = new TrasformazioneProdotto(
                processo, trasformatore, input, output,
                quantitaInput, quantitaOutput, note
        );

        return trasformazioniRepo.save(t);
    }

    public List<TrasformazioneProdotto> listaPerProcesso(Long processoId) {
        return trasformazioniRepo.findByProcessoId(processoId);
    }

    public List<TrasformazioneProdotto> listaPerTrasformatore(Long trasformatoreId) {
        return trasformazioniRepo.findByTrasformatoreId(trasformatoreId);
    }
}
