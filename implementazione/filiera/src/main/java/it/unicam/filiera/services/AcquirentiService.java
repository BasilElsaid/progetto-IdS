package it.unicam.filiera.services;

import it.unicam.filiera.builder.AcquirenteBuilder;
import it.unicam.filiera.dto.create.CreateAcquirenteRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.dto.update.UpdateAcquirenteRequest;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirentiRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcquirentiService {

    private final AcquirentiRepository acquirenteRepo;
    private final PasswordEncoder passwordEncoder;

    public AcquirentiService(AcquirentiRepository acquirenteRepo, PasswordEncoder passwordEncoder) {
        this.acquirenteRepo = acquirenteRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteResponse creaAcquirente(CreateAcquirenteRequest request) {
        Acquirente a = (Acquirente) new AcquirenteBuilder()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setEmail(request.email())
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

    public UtenteResponse patchAcquirente(Long id, UpdateAcquirenteRequest request) {
        Acquirente a = acquirenteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        if (request.email() != null) a.setEmail(request.email());
        if (request.password() != null) a.setPassword(passwordEncoder.encode(request.password()));

        return UtenteResponse.from(acquirenteRepo.save(a));
    }

}