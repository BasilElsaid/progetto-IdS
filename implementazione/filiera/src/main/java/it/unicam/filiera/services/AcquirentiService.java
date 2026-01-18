package it.unicam.filiera.services;

import it.unicam.filiera.builder.AcquirenteBuilder;
import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcquirentiService {

    private final AcquirenteRepository acquirenteRepo;
    private final PasswordEncoder passwordEncoder;

    public AcquirentiService(AcquirenteRepository acquirenteRepo, PasswordEncoder passwordEncoder) {
        this.acquirenteRepo = acquirenteRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteResponse creaAcquirente(CreateAcquirenteRequest request) {
        Acquirente a = (Acquirente) new AcquirenteBuilder()
                .setUsername(request.getUsername())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setEmail(request.getEmail())
                .build();
        Acquirente saved = acquirenteRepo.save(a);
        return UtenteResponse.from(saved);
    }

    public List<UtenteResponse> listaAcquirenti() {
        return acquirenteRepo.findAll().stream()
                .map(UtenteResponse::from)
                .collect(Collectors.toList());
    }

    public UtenteResponse getAcquirente(Long id) {
        return acquirenteRepo.findById(id)
                .map(UtenteResponse::from)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));    }

    public void deleteAcquirente(Long id) {
        if (!acquirenteRepo.existsById(id)) {
            throw new NotFoundException("Acquirente non trovato");
        }
        acquirenteRepo.deleteById(id);
    }

    public UtenteResponse patchAcquirente(Long id, CreateAcquirenteRequest request) {
        Acquirente a = acquirenteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        if (request.getEmail() != null) a.setEmail(request.getEmail());
        if (request.getPassword() != null) a.setPassword(request.getPassword());

        return UtenteResponse.from(acquirenteRepo.save(a));
    }
}