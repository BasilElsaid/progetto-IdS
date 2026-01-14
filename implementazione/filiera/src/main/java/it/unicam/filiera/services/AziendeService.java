package it.unicam.filiera.services;

import it.unicam.filiera.builder.DistributoreTipicitaBuilder;
import it.unicam.filiera.builder.ProduttoreBuilder;
import it.unicam.filiera.builder.TrasformatoreBuilder;
import it.unicam.filiera.controllers.dto.CreateAziendaRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.DistributoreTipicitaRepository;
import it.unicam.filiera.repositories.ProduttoreRepository;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AziendeService {

    private final ProduttoreRepository produttoreRepo;
    private final TrasformatoreRepository trasformatoreRepo;
    private final DistributoreTipicitaRepository distributoreRepo;

    public AziendeService(
            ProduttoreRepository produttoreRepo,
            TrasformatoreRepository trasformatoreRepo,
            DistributoreTipicitaRepository distributoreRepo
    ) {
        this.produttoreRepo = produttoreRepo;
        this.trasformatoreRepo = trasformatoreRepo;
        this.distributoreRepo = distributoreRepo;
    }

    public UtenteGenerico creaAzienda(CreateAziendaRequest request) {
        Ruolo ruolo = request.getRuolo();
        switch (ruolo) {
            case PRODUTTORE:
                Produttore p = (Produttore) new ProduttoreBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                        .setEmail(request.getEmail())
                        .build();
                return produttoreRepo.save(p);

            case TRASFORMATORE:
                Trasformatore t = (Trasformatore) new TrasformatoreBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                        .setEmail(request.getEmail())
                        .build();
                return trasformatoreRepo.save(t);

            case DISTRIBUTORE_TIPICITA:
                DistributoreTipicita d = (DistributoreTipicita) new DistributoreTipicitaBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(request.getPassword())
                        .setEmail(request.getEmail())
                        .build();
                return distributoreRepo.save(d);

            default:
                throw new BadRequestException("Ruolo non gestito dal sistema");
        }
    }
    public List<UtenteResponse> listaAziende() {
        List<UtenteResponse> out = new ArrayList<>();
        produttoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        trasformatoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        distributoreRepo.findAll().forEach(u -> out.add(UtenteResponse.from(u)));
        return out;
    }

    public UtenteResponse getAzienda(Long id) {
        return produttoreRepo.findById(id).map(UtenteResponse::from)
                .or(() -> trasformatoreRepo.findById(id).map(UtenteResponse::from))
                .or(() -> distributoreRepo.findById(id).map(UtenteResponse::from))
                .orElseThrow(() -> new NotFoundException("Azienda non trovata"));
    }

    public void deleteAzienda(Long id) {
        if (produttoreRepo.existsById(id)) {
            produttoreRepo.deleteById(id);
            return;
        }
        if (trasformatoreRepo.existsById(id)) {
            trasformatoreRepo.deleteById(id);
            return;
        }
        if (distributoreRepo.existsById(id)) {
            distributoreRepo.deleteById(id);
            return;
        }
        throw new NotFoundException("Azienda non trovata");
    }

    public UtenteResponse patchAzienda(Long id, CreateAziendaRequest request) {
        if (produttoreRepo.existsById(id)) {
            Produttore p = produttoreRepo.findById(id).get();
            if(request.getEmail() != null) p.setEmail(request.getEmail());
            if(request.getPassword() != null) p.setPassword(request.getPassword());
            return UtenteResponse.from(produttoreRepo.save(p));
        }
        if (trasformatoreRepo.existsById(id)) {
            Trasformatore t = trasformatoreRepo.findById(id).get();
            if(request.getEmail() != null) t.setEmail(request.getEmail());
            if(request.getPassword() != null) t.setPassword(request.getPassword());
            return UtenteResponse.from(trasformatoreRepo.save(t));
        }
        if (distributoreRepo.existsById(id)) {
            DistributoreTipicita d = distributoreRepo.findById(id).get();
            if(request.getEmail() != null) d.setEmail(request.getEmail());
            if(request.getPassword() != null) d.setPassword(request.getPassword());
            return UtenteResponse.from(distributoreRepo.save(d));
        }
        throw new NotFoundException("Azienda non trovata");
    }
}