package it.unicam.filiera.services;

import it.unicam.filiera.builder.AcquirenteBuilder;
import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcquirentiService {

    private final AcquirenteRepository acquirenteRepo;

    public AcquirentiService(AcquirenteRepository acquirenteRepo) {
        this.acquirenteRepo = acquirenteRepo;
    }

    public Acquirente creaAcquirente(CreateAcquirenteRequest request) {
        Acquirente a = (Acquirente) new AcquirenteBuilder()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .setEmail(request.getEmail())
                .build();
        return acquirenteRepo.save(a);
    }

    public List<UtenteResponse> listaAcquirenti() {
        return acquirenteRepo.findAll().stream()
                .map(UtenteResponse::from)
                .collect(Collectors.toList());
    }

    public UtenteResponse getAcquirente(Long id) {
        return acquirenteRepo.findById(id)
                .map(UtenteResponse::from)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));
    }

    public void deleteAcquirente(Long id) {
        if (!acquirenteRepo.existsById(id)) {
            throw new RuntimeException("Acquirente non trovato");
        }
        acquirenteRepo.deleteById(id);
    }

    public UtenteResponse patchAcquirente(Long id, CreateAcquirenteRequest request) {
        Acquirente a = acquirenteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Acquirente non trovato"));

        if (request.getEmail() != null) a.setEmail(request.getEmail());
        if (request.getPassword() != null) a.setPassword(request.getPassword());

        return UtenteResponse.from(acquirenteRepo.save(a));
    }
}